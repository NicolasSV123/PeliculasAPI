package com.nicolas.peliculasapi.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nicolas.peliculasapi.R
import com.nicolas.peliculasapi.adapters.PeliculaAdapter
import com.nicolas.peliculasapi.data.Pelicula
import com.nicolas.peliculasapi.data.PeliculasAPI
import com.nicolas.peliculasapi.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: PeliculaAdapter
    var peliculasList: List<Pelicula> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = PeliculaAdapter(peliculasList) { position ->
            val selectedPelicula = peliculasList[position]
            val imdbId = selectedPelicula.imdbID
            if (!imdbId.isNullOrEmpty()) {
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_PELICULA_ID, imdbId)
                }
                startActivity(intent)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchPeliculas(query.trim())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank() && newText.length >= 2) {
                    searchPeliculas(newText.trim())
                }
                return true
            }
        })
        searchPeliculas("Spider-man")
    }

    fun searchPeliculas(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = PeliculasAPI.getInstance().getPeliculasList(query)
            withContext(Dispatchers.Main) {
                if (response.response == "True") {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyStateTextView.visibility = View.GONE
                    peliculasList = response.peliculas ?: emptyList()
                    adapter.updateData(peliculasList)
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyStateTextView.visibility = View.VISIBLE
                    val message = if (response.error == "Too many results.") {
                        "Demasiados resultados, indica un título más específico"
                    } else {
                        "No se encontraron películas"
                    }
                    binding.emptyStateTextView.text = message
                    peliculasList = emptyList()
                    adapter.updateData(peliculasList)
                }
            }
        }
    }
}
