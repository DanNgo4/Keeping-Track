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
import androidx.fragment.app.activityViewModels
import com.example.keepingtrack.R
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.`object`.Constant
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MovieDetailFragment : Fragment() {
    private var movie: Movie? = null
    private val viewModel: MovieDetailViewModel by activityViewModels()
    private val database = Firebase.database
    private val movieRef = database.getReference(Constant.PATH_MOVIES_REFERENCE)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        super.onViewCreated(view, savedInstanceState)

        val movieId= view.findViewById<TextView>(R.id.editMovieId)
        val movieName = view.findViewById<EditText>(R.id.editMovieName)
        val movieYear = view.findViewById<EditText>(R.id.editMovieYear)
        val movieDirector = view.findViewById<EditText>(R.id.editMovieDirector)
        val movieGenre = view.findViewById<TextView>(R.id.editMovieGenre)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val movieRating = view.findViewById<EditText>(R.id.editMovieRating)
        val movieNotes = view.findViewById<EditText>(R.id.editMovieNotes)
        movie?.let {
            movieId.text = "${it.id}."
            movieName.setText(it.name)
            movieYear.setText(it.year.toString())
            movieDirector.setText(it.director)
            movieGenre.text = getString(it.genre.displayNameId)
            ratingBar.rating = it.rating
            movieRating.setText(it.rating.toString())
            movieNotes.setText(it.notes ?: "None")
        }

        // editing enabled
        val editBtn = view.findViewById<ImageButton>(R.id.editMovieBtn)
        var editing = false
        editBtn.setOnClickListener {
            editing = !editing
            toggleEditing(movieName, movieYear, movieDirector, ratingBar, movieRating, movieNotes, editing)

            if (!editing) {
                saveChanges(movie, movieName, movieYear, movieDirector, ratingBar, movieNotes)
            }
        }

        // return to previous fragment
        val backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // delete movie
        val deleteBtn = view.findViewById<ImageButton>(R.id.deleteBtn2)
        deleteBtn.setOnClickListener {
            movie?.let { movie ->
                // Call ViewModel to delete the movie from the database
                viewModel.deleteMovie(movie)

                // return to previous fragment
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun toggleEditing(
        movieName: EditText,
        movieYear: EditText,
        movieDirector: EditText,
        ratingBar: RatingBar,
        movieRating: EditText,
        movieNotes: EditText,
        editing: Boolean
    ) {
        movieName.isEnabled = editing
        movieYear.isEnabled = editing
        movieDirector.isEnabled = editing
        ratingBar.setIsIndicator(!editing)
        movieRating.isEnabled = editing
        movieNotes.isEnabled = editing

        val editBtn = requireView().findViewById<ImageButton>(R.id.editMovieBtn)
        if (editing) {
            editBtn.setImageResource(R.drawable.ic_save)
        } else {
            editBtn.setImageResource(R.drawable.ic_edit)
        }
    }

    // Save changes to Firebase
    private fun saveChanges(
        movie: Movie?,
        movieName: EditText,
        movieYear: EditText,
        movieDirector: EditText,
        ratingBar: RatingBar,
        movieNotes: EditText
    ) {
        movie?.let {
            val updatedMovie = Movie(
                id = it.id,
                name = movieName.text.toString(),
                year = movieYear.text.toString().toIntOrNull() ?: it.year,
                director = movieDirector.text.toString(),
                genre = it.genre, // assuming genre doesn't change in this form
                rating = ratingBar.rating,
                notes = movieNotes.text.toString()
            )

            // Update movie in Firebase
            movieRef.child(it.id.toString())
                .setValue(updatedMovie)
                .addOnSuccessListener {
                    // Optionally update the UI (RecyclerView) here by notifying the ViewModel
                    viewModel.updateMovie(updatedMovie)
                }
        }
    }
}