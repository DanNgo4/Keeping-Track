package com.example.keepingtrack.ui.movielist

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.keepingtrack.R
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.`object`.Constant
import com.example.keepingtrack.ui.moviedetail.MovieDetailFragment
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.io.Serializable

class MovieListFragment() : Fragment() {
    private lateinit var values: List<Movie>
    private lateinit var recyclerView: RecyclerView
    private var columnCount = 1
    private val database = Firebase.database
    private val movieRef = database.getReference(Constant.PATH_MOVIES_REFERENCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT, 1)
            values = it.getSerializable(ARG_MOVIE_LIST) as List<Movie>
        } ?: run {
            values = emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        // Find the RecyclerView by ID
        recyclerView = view.findViewById(R.id.recycler_view)

        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            // Pass a lambda function as the listener for movie clicks
            adapter = MovieListRecyclerViewAdapter(
                values,

                {
                    movie -> showDetail(movie)
                },

                {
                    movie -> deleteMovie(movie)
                })
        }

        return view
    }

    private fun showDetail(movie: Movie) {
        val fragment = MovieDetailFragment.newInstance(movie)

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteMovie(movie: Movie) {
        val movieId = movie.id.toString()

        // Remove the movie from Firebase
        movieRef.child(movieId)
            .removeValue()
            .addOnSuccessListener { // Remove the movie from the UI (RecyclerView)
                val position = values.indexOf(movie)
                if (position >= 0) {
                    (values as MutableList).removeAt(position)
                    recyclerView.adapter?.notifyItemRemoved(position)
                }
            }
    }

    companion object {
        private const val ARG_MOVIE_LIST = "movie_list"
        private const val ARG_COLUMN_COUNT = "column_count"

        @JvmStatic
        fun newInstance(movies: List<Movie>): MovieListFragment {
            return MovieListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_MOVIE_LIST, ArrayList(movies))
                }
            }
        }
    }
}