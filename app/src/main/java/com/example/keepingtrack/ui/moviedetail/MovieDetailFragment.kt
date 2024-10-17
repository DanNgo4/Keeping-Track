package com.example.keepingtrack.ui.moviedetail

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import com.example.keepingtrack.R
import com.example.keepingtrack.data.Movie

class MovieDetailFragment : Fragment() {
    private var movie: Movie? = null

    companion object {
        private const val ARG_MOVIE_DETAIL = "movie_detail"

        fun newInstance(movie: Movie) : MovieDetailFragment {
            return MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_MOVIE_DETAIL, movie)
                }
            }
        }
    }

    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve and assign the Movie object from arguments
        movie = arguments?.getSerializable(ARG_MOVIE_DETAIL) as? Movie
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("Detail Fragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        val movieId= view.findViewById<TextView>(R.id.editMovieId)
        val movieName = view.findViewById<EditText>(R.id.editMovieName)
        val movieYear = view.findViewById<EditText>(R.id.editMovieYear)
        val movieDirector = view.findViewById<EditText>(R.id.editMovieDirector)
        val movieGenre = view.findViewById<EditText>(R.id.editMovieGenre)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val movieRating = view.findViewById<EditText>(R.id.editMovieRating)
        val movieNotes = view.findViewById<EditText>(R.id.editMovieNotes)
        movie?.let {
            movieId.text = "${it.id}."
            movieName.setText(it.name)
            movieYear.setText(it.year.toString())
            movieDirector.setText(it.director)
            movieGenre.setText(getString(it.genre.displayNameId))
            ratingBar.rating = it.rating
            movieRating.setText(it.rating.toString())
            movieNotes.setText(it.notes ?: "None")
        }

        val btn = view.findViewById<ImageButton>(R.id.editMovieBtn)
        var editing = false
        btn.setOnClickListener {
            editing = !editing
            movieName.isEnabled = editing
            movieYear.isEnabled = editing
            movieDirector.isEnabled = editing
            movieGenre.isEnabled = editing
            ratingBar.setIsIndicator(!editing)
            movieRating.isEnabled = editing
            movieNotes.isEnabled = editing

            if (editing) {
                btn.setImageResource(R.drawable.ic_save)

                movieName.background = resources.getDrawable(android.R.drawable.edit_text, null)
                movieName.requestFocus()
                movieYear.background = resources.getDrawable(android.R.drawable.edit_text, null)
                movieYear.requestFocus()
                movieDirector.background = resources.getDrawable(android.R.drawable.edit_text, null)
                movieDirector.requestFocus()
                movieGenre.background = resources.getDrawable(android.R.drawable.edit_text, null)
                movieGenre.requestFocus()
                movieRating.background = resources.getDrawable(android.R.drawable.edit_text, null)
                movieRating.requestFocus()
                movieNotes.background = resources.getDrawable(android.R.drawable.edit_text, null)
                movieNotes.requestFocus()
            } else {
                btn.setImageResource(R.drawable.ic_edit)

                movieName.background = null
                movieYear.background = null
                movieDirector.background = null
                movieGenre.background = null
                movieRating.background = null
                movieNotes.background = null
            }
        }
    }
}