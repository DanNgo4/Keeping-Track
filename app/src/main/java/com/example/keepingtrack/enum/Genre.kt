package com.example.keepingtrack.enum

import com.example.keepingtrack.R

enum class Genre(val displayNameId: Int) {
    NONE(R.string.genre_none),
    ROMANCE(R.string.genre_romance),
    DRAMA(R.string.genre_drama),
    ACTION(R.string.genre_action),
    HORROR(R.string.genre_horror),
    SCIFI(R.string.genre_scifi),
    COMEDY(R.string.genre_comedy),
    ANIMATION(R.string.genre_animation)
}