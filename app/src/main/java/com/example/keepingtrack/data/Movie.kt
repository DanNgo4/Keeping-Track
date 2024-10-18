package com.example.keepingtrack.data
import com.example.keepingtrack.enum.Genre
import java.io.Serializable

data class Movie(val id: Int = 0,
                 var name: String= "",
                 var year: Int = 0,
                 var director: String = "",
                 var rating: Float = 0f,
                 var genre: Genre= Genre.NONE,
                 var notes: String? = null): Serializable {
}
