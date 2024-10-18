package com.example.keepingtrack.ui.addmovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.example.keepingtrack.R
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.enum.Genre
import com.example.keepingtrack.`object`.Constant
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AddMovieFragment : Fragment() {
    private val database = Firebase.database
    private val movieRef = database.getReference(Constant.PATH_MOVIES_REFERENCE)
    private lateinit var selectedGenre: Genre
    private var newMovieId: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch the last movie's ID from the database
        fetchLastMovieId()

        // return to previous fragment
        val backBtn = view.findViewById<ImageButton>(R.id.backBtn2)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val movieName = view.findViewById<EditText>(R.id.addName)
        val movieYear = view.findViewById<EditText>(R.id.addYear)
        val movieDirector = view.findViewById<EditText>(R.id.addDirector)
        val movieGenre = view.findViewById<Spinner>(R.id.spinner)
        val movieRating = view.findViewById<EditText>(R.id.addRating)
        val movieNotes = view.findViewById<EditText>(R.id.addNotes)

        // Set up the Spinner
        val genreAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Genre.entries.toTypedArray() // Use the enum values directly
        )

        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        movieGenre.adapter = genreAdapter

        // Set a listener to get the selected genre
        movieGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected genre from the spinner
                selectedGenre = parent.getItemAtPosition(position) as Genre
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedGenre = Genre.NONE
            }
        }

        val addBtn = view.findViewById<Button>(R.id.addMovieBtn)
        addBtn.setOnClickListener {
            val movie = Movie(
                newMovieId,
                movieName.text.toString(),
                movieYear.text.toString().toInt(),
                movieDirector.text.toString(),
                movieRating.text.toString().toFloat(),
                selectedGenre,
                movieNotes.text.toString()
            )

            addMovie(movie)
        }
    }

    private fun fetchLastMovieId() {
        // Query to get the last movie by ordering by the ID key
        movieRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Get the last movie's ID and increment it by 1
                    for (movieSnapshot in snapshot.children) {
                        val lastMovieId = movieSnapshot.key?.toIntOrNull() ?: 0
                        newMovieId = lastMovieId + 1
                    }
                } else {
                    // If no movies exist, start with ID 1
                    newMovieId = 1
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch last movie ID: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addMovie(movie: Movie) {
        movieRef.child(movie.id.toString())
            .setValue(movie)
            .addOnSuccessListener {
                // Navigate back to the movie list or show a success message
                requireActivity().supportFragmentManager.popBackStack()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddMovieFragment()
    }
}