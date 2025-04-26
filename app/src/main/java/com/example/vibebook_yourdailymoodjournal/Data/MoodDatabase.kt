package com.example.vibebook_yourdailymoodjournal.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MoodEntry::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MoodDatabase : RoomDatabase(){


    abstract fun moodDao() : MoodDao

    companion object{
    @Volatile
    private var INSTANCE : MoodDatabase? = null

        //Creating Database
    fun getDatabase(context: Context) : MoodDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MoodDatabase::class.java,
                "mood_database"
            )
                .fallbackToDestructiveMigration(true)
                .build()
            INSTANCE = instance
            instance
        }
    }
    }
}