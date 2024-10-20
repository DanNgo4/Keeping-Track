package com.example.keepingtrack.ui.movielist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.keepingtrack.MainActivity
import com.example.keepingtrack.R
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.`object`.Constant
import com.example.keepingtrack.ui.moviedetail.MovieDetailFragment
import com.example.keepingtrack.ui.moviedetail.MovieDetailViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MovieListFragment() : Fragment() {
    private lateinit var values: MutableList<Movie>
    private lateinit var recyclerView: RecyclerView
    private val viewModel: MovieDetailViewModel by activityViewModels()
    private val movieRef = Firebase.database.getReference(Constant.PATH_MOVIES_REFERENCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        values = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set the adapter for RecyclerView
        recyclerView.adapter = MovieListRecyclerViewAdapter(
            values,
            { movie -> showDetail(movie) },
            { movie -> deleteMovie(movie) }
        )

        // Observe LiveData for updates and deletions from ViewModel
        viewModel.deletedMovie.observe(viewLifecycleOwner) { movie ->
            val position = values.indexOf(movie)
            if (position >= 0) {
                values.removeAt(position)
                recyclerView.adapter?.notifyItemRemoved(position)
            }
        }

        viewModel.updatedMovie.observe(viewLifecycleOwner) { updatedMovie ->
            val index = values.indexOfFirst { it.id == updatedMovie.id }
            if (index >= 0) {
                values[index] = updatedMovie
                recyclerView.adapter?.notifyItemChanged(index)
            }
        }

        fetchMoviesFromFirebase()
    }

    private fun fetchMoviesFromFirebase() {
        movieRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                values.clear() // Clear the current list before adding new data

                for (snapshot in dataSnapshot.children) {
                    val movie = snapshot.getValue(Movie::class.java)
                    movie?.let { values.add(it) }
                }

                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(Constant.TAG_MOVIE_LIST_FRAGMENT, "Failed to read data from Firebase", error.toException())
            }
        })
    }

    // Open an individual movie for further details and edit
    private fun showDetail(movie: Movie) {
        val fragment = MovieDetailFragment.newInstance(movie)

        (requireActivity() as MainActivity).replaceFragment(fragment, Constant.TAG_MOVIE_DETAIL_FRAGMENT)
    }

    private fun deleteMovie(movie: Movie) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Movie")
        builder.setMessage("Confirm delete ${movie.name} (${movie.year})?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            val movieId = movie.id.toString()

            // Remove the movie from Firebase
            movieRef.child(movieId)
                .removeValue()
                .addOnSuccessListener { // Remove the movie from the UI (RecyclerView)
                    val position = values.indexOf(movie)
                    if (position >= 0) {
                        values.removeAt(position)
                        recyclerView.adapter?.notifyItemRemoved(position)
                    }
                }

            dialog.dismiss()

            Toast.makeText(requireContext(), "Deleted ${movie.name} (${movie.year}).", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MovieListFragment()
    }
}
