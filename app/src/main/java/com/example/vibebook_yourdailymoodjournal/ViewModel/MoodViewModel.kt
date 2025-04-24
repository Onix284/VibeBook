package com.example.vibebook_yourdailymoodjournal.ViewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibebook_yourdailymoodjournal.Data.MoodDao
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoodViewModel(private val  moodDao: MoodDao) : ViewModel() {

    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries

    init {
        loadMoodEntries()
    }

    //Load the Mood Entries With the database
    private fun loadMoodEntries(){
        viewModelScope.launch {
            _moodEntries.value = moodDao.getAllMood()
        }
    }
}