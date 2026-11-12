package com.nicolas.peliculasapi.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.nicolas.peliculasapi.data.Pelicula
import com.nicolas.peliculasapi.data.PeliculasAPI
import com.nicolas.peliculasapi.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PELICULA_ID = "EXTRA_PELICULA_ID"
    }
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imdbId = intent.getStringExtra(EXTRA_PELICULA_ID)
        imdbId?.let {
            fetchPeliculaDetail(it)
        }
    }

    fun fetchPeliculaDetail(imdbId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val pelicula = PeliculasAPI.getInstance().getPeliculaByID(imdbId)
            withContext(Dispatchers.Main) {
                showPeliculaDetail(pelicula)
            }
        }
    }

    fun showPeliculaDetail(pelicula: Pelicula) {
        binding.titleTextView.text = pelicula.title ?: "Sin título"
        binding.sinopsisTextView.text = pelicula.plot ?: "No disponible"
        binding.durationChip.text = pelicula.runtime ?: "N/A"
        binding.directorChip.text = pelicula.director ?: "N/A"
        binding.genreChip.text = pelicula.genre ?: "N/A"
        binding.countryChip.text = pelicula.country ?: "N/A"

        if (!pelicula.poster.isNullOrEmpty() && pelicula.poster != "N/A") {
            Picasso.get()
                .load(pelicula.poster)
                .into(binding.posterImageView)
        }
    }
}
