package com.example.vibebook_yourdailymoodjournal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vibebook_yourdailymoodjournal.Data.MoodDao

//Viewmodel Factory
class ViewModelFactory(
    private val dao: MoodDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}