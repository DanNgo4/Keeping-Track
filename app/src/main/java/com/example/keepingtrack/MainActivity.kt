package com.example.keepingtrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.keepingtrack.data.Movie
import com.example.keepingtrack.enum.Genre
import com.example.keepingtrack.`object`.Constant
import com.example.keepingtrack.ui.SharedViewModel
import com.example.keepingtrack.ui.addmovie.AddMovieFragment
import com.example.keepingtrack.ui.movielist.MovieListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {
    private val movieRef = Firebase.database.getReference(Constant.PATH_MOVIES_REFERENCE)
    private val viewmodel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialise values and write to Firebase
        // initValuesToFirebase()

        if (savedInstanceState == null) {
            loadDefaultFragment()
        } else {
            // Restore the current fragment using the saved fragment tag from the shared ViewModel
            viewmodel.currentFragmentTag?.let { fragmentTag ->
                val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
                fragment?.let {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, it, fragmentTag)
                        .commit()
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { // Navigate to the MovieListFragment (Home)
                    replaceFragment(MovieListFragment.newInstance(), Constant.TAG_MOVIE_LIST_FRAGMENT)
                    true
                }
                R.id.nav_add_movie -> { // Navigate to the AddMovieFragment
                    replaceFragment(AddMovieFragment.newInstance(), Constant.TAG_ADD_MOVIE_FRAGMENT)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MovieListFragment(), Constant.TAG_MOVIE_LIST_FRAGMENT)
            .commit()

        viewmodel.currentFragmentTag = Constant.TAG_MOVIE_LIST_FRAGMENT
    }

    // replacing fragment and save it as the current state
    fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment, tag)
            .addToBackStack(null)
            .commit()

        viewmodel.currentFragmentTag = tag
    }

    private fun initValuesToFirebase() {
        val movies = mutableListOf<Movie>()

        movies.add(Movie(1, "The Gentlemen", 2019, "Guy Ritchie", 7.8f, Genre.ACTION, null))
        movies.add(Movie(2, "Mad Max: Fury Road", 2015, "George Miller", 8.1f, Genre.ACTION, "A delightful romantic musical about a small-town florist’s quest to bring happiness to a bustling city with charming dance numbers and catchy songs."))
        movies.add(Movie(3, "Pride and Prejudice", 2005, "Joe Wright", 7.8f, Genre.ROMANCE, "Based on the classic novel"))
        movies.add(Movie(4, "La La Land", 2016, "Damien Chazelle", 8.0f, Genre.ROMANCE, "A musical romance in Los Angeles"))
        movies.add(Movie(5, "The Godfather", 1972, "Francis Ford Coppola", 9.2f, Genre.DRAMA, "Crime family epic"))
        movies.add(Movie(6, "Forrest Gump", 1994, "Robert Zemeckis", 8.8f, Genre.DRAMA, "Story of an extraordinary life"))
        movies.add(Movie(7, "A Quiet Place", 2018, "John Krasinski", 7.5f, Genre.HORROR, "Silence is survival"))
        movies.add(Movie(8, "Get Out", 2017, "Jordan Peele", 7.7f, Genre.HORROR, "Social thriller with a twist"))
        movies.add(Movie(9, "Inception", 2010, "Christopher Nolan", 8.8f, Genre.SCIFI, "Mind-bending exploration of dreams"))
        movies.add(Movie(10, "The Matrix", 1999, "The Wachowskis", 8.7f, Genre.SCIFI, "Explores virtual reality and control"))
        movies.add(Movie(11, "Superbad", 2007, "Greg Mottola", 7.6f, Genre.COMEDY, "High school comedy about friendship"))
        movies.add(Movie(12, "The Grand Budapest Hotel", 2014, "Wes Anderson", 8.1f, Genre.COMEDY, "Stylish and quirky caper"))
        movies.add(Movie(13, "Spirited Away", 2001, "Hayao Miyazaki", 8.6f, Genre.ANIMATION, "A magical journey in a spirit world"))
        movies.add(Movie(14, "Toy Story", 1995, "John Lasseter", 8.3f, Genre.ANIMATION, "The first fully computer-animated film"))
        movies.add(Movie(15, "Her", 2013, "Spike Jonze", 8.0f, Genre.NONE, "A love story with an AI twist"))
        movies.add(Movie(16, "Whiplash", 2014, "Damien Chazelle", 8.5f, Genre.NONE, "An intense story about ambition"))
        movies.add(Movie(17, "The Notebook", 2004, "Nick Cassavetes", 7.8f, Genre.ROMANCE, "A classic love story"))
        movies.add(Movie(18, "Edge of Tomorrow", 2014, "Doug Liman", 7.9f, Genre.ACTION, "A soldier relives a day in an alien war"))

        for (movie in movies) {
            val movieId = movie.id.toString() // use movie ID as the key
            movieRef.child(movieId).setValue(movie)
        }
    }
}
