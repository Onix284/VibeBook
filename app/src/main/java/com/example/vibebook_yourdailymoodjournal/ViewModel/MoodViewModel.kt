package com.example.vibebook_yourdailymoodjournal.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibebook_yourdailymoodjournal.Data.MoodDao
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MoodViewModel(private val  moodDao: MoodDao) : ViewModel() {

    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries


    init {
        loadMoodEntries()
    }

    fun addMoodEntry(entry: MoodEntry){
        viewModelScope.launch {
            moodDao.insertMood(entry)
            loadMoodEntries()
        }
    }

    fun deleteMood(entry: MoodEntry){
        viewModelScope.launch {
            moodDao.deleteMood(entry)
            loadMoodEntries()
        }
    }

    //Load the Mood Entries With the database
    private fun loadMoodEntries(){
        viewModelScope.launch {
           _moodEntries.value = moodDao.getAllMood()
        }
    }
}