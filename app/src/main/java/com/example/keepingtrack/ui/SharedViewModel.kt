package com.example.keepingtrack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.`object`.Constant
import com.google.firebase.Firebase
import com.google.firebase.database.database

// shared ViewModel for all 3 Fragments
class SharedViewModel : ViewModel()
{
    // store the current fragment to save state
    var currentFragmentTag: String? = null

    // MutableLiveData to notify when a movie is deleted
    private val _deletedMovie = MutableLiveData<Movie>()
    val deletedMovie: LiveData<Movie> get() = _deletedMovie

    // MutableLiveData to notify when a movie is updated
    private val _updatedMovie = MutableLiveData<Movie>()
    val updatedMovie: LiveData<Movie> = _updatedMovie

    private val movieRef = Firebase.database.getReference(Constant.PATH_MOVIES_REFERENCE)

    // Delete Movie
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

    // Create or Update Movie
    fun updateMovie(movie: Movie, onSuccess: (() -> Unit)? = null) {
        movieRef.child(movie.id.toString())
            .setValue(movie)
            .addOnSuccessListener {
                _updatedMovie.value = movie

                // invokes callback function
                onSuccess?.invoke()
            }
    }
}
