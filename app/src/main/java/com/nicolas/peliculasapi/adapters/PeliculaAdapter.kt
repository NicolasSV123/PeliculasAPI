package com.nicolas.peliculasapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nicolas.peliculasapi.data.Pelicula
import com.nicolas.peliculasapi.databinding.ItemPeliculaBinding
import com.squareup.picasso.Picasso

class PeliculaAdapter (
    var items: List<Pelicula>,
    val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<PeliculaViewHolder>(){

    override fun onCreateViewHolder( parent: ViewGroup,  viewType: Int ): PeliculaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPeliculaBinding.inflate(layoutInflater, parent, false)
        return PeliculaViewHolder(binding)
    }

    override fun onBindViewHolder( holder: PeliculaViewHolder,  position: Int ) {
        val pelicula = items[position]
        holder.render(pelicula)

        holder.binding.peliculaCard.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(dataSet: List<Pelicula>){
        items = dataSet
        notifyDataSetChanged()
    }
}

class PeliculaViewHolder(val binding: ItemPeliculaBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render (item: Pelicula) {
        binding.titleTextView.text = item.title ?: ""
        binding.yearTextView.text = item.year ?: ""
        if (!item.poster.isNullOrEmpty() && item.poster != "N/A") {
            Picasso.get().load(item.poster).into(binding.posterImageView)
        } else {
            binding.posterImageView.setImageDrawable(null)
        }
    }
}
