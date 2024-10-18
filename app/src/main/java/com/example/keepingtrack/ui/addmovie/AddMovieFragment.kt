package com.example.keepingtrack.ui.addmovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.keepingtrack.R
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.`object`.Constant
import com.google.firebase.Firebase
import com.google.firebase.database.database

class AddMovieFragment : Fragment() {
    private val database = Firebase.database
    private val movieRef = database.getReference(Constant.PATH_MOVIES_REFERENCE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun addMovie(movie: Movie) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = AddMovieFragment()
    }
}