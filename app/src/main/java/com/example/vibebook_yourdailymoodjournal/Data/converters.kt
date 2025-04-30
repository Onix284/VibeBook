package com.example.vibebook_yourdailymoodjournal.Data

import android.net.Uri
import androidx.room.TypeConverter
import androidx.core.net.toUri

class Converters {

    @TypeConverter
    fun fromMoodEmoji(moodEmoji: MoodEmoji) : String{
        return moodEmoji.name
    }

    @TypeConverter
    fun toEmoji(name : String) : MoodEmoji{
        return MoodEmoji.valueOf(name)
    }


    @TypeConverter
    fun fromUriListToString(value: List<Uri>?): String {
        return value?.joinToString(",") { it.toString() } ?: ""
    }

    @TypeConverter
    fun fromStringToUriList(value: String): List<Uri> {
        return if (value.isEmpty()) emptyList()
        else value.split(",").map { it.toUri() }
    }
}