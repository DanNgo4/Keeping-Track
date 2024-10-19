package com.example.keepingtrack.ui

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel()
{
    // store the current fragment to save state
    var currentFragmentTag: String? = null
}