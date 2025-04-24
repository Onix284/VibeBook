package com.example.vibebook_yourdailymoodjournal.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(moodEntry: MoodEntry)

    @Delete
    suspend fun deleteMood(moodEntry: MoodEntry)

    @Query("SELECT * FROM mood_entries")
    fun getAllMood() : Flow<List<MoodEntry>>
}