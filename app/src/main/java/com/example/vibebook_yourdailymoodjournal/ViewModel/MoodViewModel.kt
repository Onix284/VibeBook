package com.example.vibebook_yourdailymoodjournal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibebook_yourdailymoodjournal.Data.MoodDao
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.Network.GetQuotes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoodViewModel(private val  moodDao: MoodDao) : ViewModel() {

    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries


    //Quotes Value
    private val _quotes = MutableStateFlow<List<String>>(emptyList())
    val quotes : StateFlow<List<String>> = _quotes

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

    fun getMood(id: Int) : Flow<MoodEntry?>{
            return moodDao.getMoodById(id)
    }

    //Load the Mood Entries With the database
    private fun loadMoodEntries(){
        viewModelScope.launch {
           _moodEntries.value = moodDao.getAllMood()
        }
    }

    fun getQuote(){
        viewModelScope.launch {
            try {

                val response = GetQuotes()

                if(!response.map { it.q }.isEmpty())
                {
                    _quotes.value = response.map { it.q }
                    Log.d("Quote", "getQuote: ${_quotes.value}")
                }
                else {
                    Log.d("QuoteError", "No quotes found")
                }
            }
            catch (e : Exception){
                Log.d("QuoteError", "Failed, ${e.message}")
            }
        }
    }

}