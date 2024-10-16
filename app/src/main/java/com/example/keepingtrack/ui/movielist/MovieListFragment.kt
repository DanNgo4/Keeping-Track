package com.example.keepingtrack.ui.movielist

import android.os.Bundle
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

/**
 * A fragment representing a list of Items.
 */
class MovieListFragment() : Fragment() {
    private lateinit var values: List<Movie>
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT, 1) // Default to 1 if not provided
            values = it.getSerializable(ARG_MOVIE_LIST) as List<Movie>
            Log.i("fragment", "fragment opened with ${values.size} movies")
        } ?: run {
            // Handle the case where no arguments were provided
            values = emptyList() // Initialize to an empty list to avoid uninitialized access
            Log.i("fragment", "fragment opened with no movies")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        // Find the RecyclerView by ID
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MovieListRecyclerViewAdapter(values)
        }
        return view
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