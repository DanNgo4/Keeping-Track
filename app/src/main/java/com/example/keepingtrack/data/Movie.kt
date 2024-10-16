package com.example.keepingtrack.data
import android.os.Parcel
import android.os.Parcelable
import com.example.keepingtrack.enum.Genre
import kotlinx.serialization.Serializable

@Serializable
data class Movie(val id: Int,
                 var name: String,
                 var year: Int,
                 var director: String,
                 var rating: Float,
                 var genre: Genre,
                 var notes: String?) {

}
