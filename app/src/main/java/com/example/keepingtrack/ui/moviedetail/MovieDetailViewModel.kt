package com.example.keepingtrack.ui.moviedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.`object`.Constant
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MovieDetailViewModel : ViewModel() {
    private val database = Firebase.database
    private val movieRef = database.getReference(Constant.PATH_MOVIES_REFERENCE)

    fun deleteMovie(movie: Movie) {
        val movieId = movie.id.toString()

        // Remove the movie from Firebase
        movieRef.child(movieId).removeValue()
            .addOnSuccessListener {
                Log.d("MovieDetailViewModel", "Movie ${movie.name} deleted from Firebase.")
            }
            .addOnFailureListener { e ->
                Log.e("MovieDetailViewModel", "Failed to delete movie ${movie.name}: ${e.message}")
            }
    }
}