package com.example.vibebook_yourdailymoodjournal.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MoodEntry::class], version = 1, exportSchema = false)

abstract class MoodDatabase : RoomDatabase(){

    abstract fun moodDao() : MoodDao

    @Volatile
    private var INSTANCE : MoodDatabase? = null

    fun getDatabase(context: Context) : MoodDatabase{
        return INSTANCE ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MoodDatabase::class.java,
                "mood_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}