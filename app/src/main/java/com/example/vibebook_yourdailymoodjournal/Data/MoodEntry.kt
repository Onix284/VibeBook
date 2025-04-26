package com.example.vibebook_yourdailymoodjournal.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Entities in the database table
@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val mood : MoodEmoji?,
    val note : String?,
    val dateTime : String?
)

//Enum Class to define emojis
enum class MoodEmoji(val emoji : String){
    RAD("😄"),
    GOOD("🙂"),
    MEH("😐"),
    BAD("🙁"),
    AWFUL("😢")
}

