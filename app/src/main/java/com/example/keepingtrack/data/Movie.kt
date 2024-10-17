package com.example.keepingtrack.data
import com.example.keepingtrack.enum.Genre
import java.io.Serializable

data class Movie(val id: Int,
                 var name: String,
                 var year: Int,
                 var director: String,
                 var rating: Float,
                 var genre: Genre,
                 var notes: String?): Serializable {
}
