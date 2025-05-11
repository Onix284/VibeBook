package com.example.vibebook_yourdailymoodjournal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibebook_yourdailymoodjournal.Data.MoodDao
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.Network.GetQuotes
import com.example.vibebook_yourdailymoodjournal.Network.QuotesResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoodViewModel(private val  moodDao: MoodDao) : ViewModel() {

    //Mood Response
    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries


    //Quotes Response
    private val _quotes = MutableStateFlow<List<QuotesResponseItem>>(emptyList())
    val quotes : StateFlow<List<QuotesResponseItem>> = _quotes


    //Quotes Screen State
    private val _state = MutableStateFlow(
        QuoteState(
            isLoading = false,
            quoteResponse = null,
            error = null
        )
    )

    val state =_state.asStateFlow()

    //Load All Moods On Home Screen
    init {
        loadMoodEntries()
    }

    //Adds New Mood to The Database
    fun addMoodEntry(entry: MoodEntry){
        viewModelScope.launch {
            moodDao.insertMood(entry)
            loadMoodEntries()
        }
    }

    //Deletes Existing Mood From The Database
    fun deleteMood(entry: MoodEntry){
        viewModelScope.launch {
            moodDao.deleteMood(entry)
            loadMoodEntries()
        }
    }

    //Get Specific Mood By ID For ViewMood Details Screen
    fun getMood(id: Int) : Flow<MoodEntry?>{
            return moodDao.getMoodById(id)
    }

    //Load the Mood Entries With the database
    private fun loadMoodEntries(){
        viewModelScope.launch {
           _moodEntries.value = moodDao.getAllMood()
        }
    }

    //Get Quote From Ktor Client
    fun getQuote(){
        viewModelScope.launch {

            //Loading
            _state.update {
                it.copy(isLoading = true, error = null)
            }

            //Store API Response to Response
            try {
                val response = GetQuotes()

                if(!response.isEmpty())
                {
                    _quotes.value = response

                    _state.update {
                        it.copy(isLoading = false, error = null)
                    }
                }

                else {
                    _state.update {
                        it.copy(isLoading = false, error = null)
                    }
                    Log.d("QuoteError", "No quotes found")
                }
            }
            // Throw Exception
            catch (e : Exception){
                _state.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
}

//Quote State Manager Data Class
data class QuoteState(
    val isLoading: Boolean = false,
    val quoteResponse : QuotesResponseItem? = null,
    val error : String? = null
)