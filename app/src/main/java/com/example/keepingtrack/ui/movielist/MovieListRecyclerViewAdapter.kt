package com.example.keepingtrack.ui.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString

import androidx.recyclerview.widget.RecyclerView
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.databinding.FragmentMovieItemBinding

class MovieListRecyclerViewAdapter(
    private val values: List<Movie>,
//    private val listener: (Movie) -> Unit
) : RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentMovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = values[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: FragmentMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(movie: Movie) {
                binding.movieId.text= "${movie.id}."
                binding.movieName.text = movie.name
                binding.movieYear.text = "(${movie.year})"
                binding.movieGenre.text = binding.root.context.getString(movie.genre.displayNameId)
                binding.movieDirector.text = movie.director
                binding.movieRating.text = movie.rating.toString()
                binding.movieNotes.text = movie.notes ?: "None"
//                binding.movieYear.text = movie.year.toString()
//                binding.movieRating.text = movie.rating.toString()
//                binding.movieGenre.text = movie.genre.toString()
//                binding.movieDirector.text = movie.director
//                binding.movieNotes.text = movie.notes
            }
        }
    }