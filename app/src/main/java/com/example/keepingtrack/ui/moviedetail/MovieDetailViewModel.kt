package com.example.keepingtrack.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.`object`.Constant
import com.google.firebase.Firebase
import com.google.firebase.database.database

// shared ViewModel for both MovieDetailFragment and MovieListFragment
class MovieDetailViewModel : ViewModel() {
    // MutableLiveData to notify when a movie is deleted
    private val _deletedMovie = MutableLiveData<Movie>()
    val deletedMovie: LiveData<Movie> get() = _deletedMovie

    // MutableLiveData to notify when a movie is updated
    private val _updatedMovie = MutableLiveData<Movie>()
    val updatedMovie: LiveData<Movie> = _updatedMovie

    private val database = Firebase.database
    private val movieRef = database.getReference(Constant.PATH_MOVIES_REFERENCE)

    fun deleteMovie(movie: Movie) {
        // Remove the movie from Firebase
        movieRef.child(movie.id.toString())
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Notify observers that the movie was deleted
                    _deletedMovie.value = movie
                }
            }
    }

    fun updateMovie(movie: Movie) {
        _updatedMovie.value = movie
    }
}