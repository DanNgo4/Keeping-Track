//package com.example.keepingtrack.movielist.placeholder
//
//import com.example.keepingtrack.data.Movie
//import java.util.ArrayList
//import java.util.HashMap
//
///**
// * Helper class for providing sample content for user interfaces created by
// * Android template wizards.
// */
//object PlaceholderContent {
//
//    /**
//     * An array of sample (placeholder) items.
//     */
//    val ITEMS: MutableList<Movie> = ArrayList()
//
//    /**
//     * A map of sample (placeholder) items, by ID.
//     */
//    //val ITEM_MAP: MutableMap<String, Movie> = HashMap()
//
//    private const val COUNT = 25
//
//    init {
//        // Add some sample items.
//        for (i in 1..COUNT) {
//            addItem(createPlaceholderItem(i))
//        }
//    }
//
//    private fun addItem(item: Movie) {
//        ITEMS.add(item)
//        //ITEM_MAP.put(Moive.id, item)
//    }
//
//    private fun createPlaceholderItem(position: Int): Movie {
//        return Movie(position.toString(), "Item " + position, makeDetails(position))
//    }
//
//    private fun makeDetails(position: Int): String {
//        val builder = StringBuilder()
//        builder.append("Details about Item: ").append(position)
//        for (i in 0..position - 1) {
//            builder.append("\nMore details information here.")
//        }
//        return builder.toString()
//    }
//
//    /**
//     * A placeholder item representing a piece of content.
//     */
//    data class PlaceholderItem(val id: String, val content: String, val details: String) {
//        override fun toString(): String = content
//    }
//}