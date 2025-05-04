package com.example.vibebook_yourdailymoodjournal.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


//Data Access objects, which declares functions to be performed
@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(moodEntry: MoodEntry)

    @Delete
    suspend fun deleteMood(moodEntry: MoodEntry)

    @Query("SELECT * FROM mood_entries ORDER BY dateTime DESC")
    suspend fun getAllMood() : List<MoodEntry>

    @Query("SELECT * FROM mood_entries WHERE id = :id")
    fun getMoodById(id: Int): Flow<MoodEntry?>
}