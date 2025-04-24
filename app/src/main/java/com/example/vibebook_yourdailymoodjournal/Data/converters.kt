package com.example.vibebook_yourdailymoodjournal.Data

import androidx.room.TypeConverter
import androidx.room.TypeConverters

class Converters {

    @TypeConverter
    fun fromMoodEmoji(moodEmoji: MoodEmoji) : String{
        return moodEmoji.name
    }

    @TypeConverter
    fun toEmoji(name : String) : MoodEmoji{
        return MoodEmoji.valueOf(name)
    }

}