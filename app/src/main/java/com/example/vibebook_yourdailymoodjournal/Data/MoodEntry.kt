package com.example.vibebook_yourdailymoodjournal.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey  val id : String = UUID.randomUUID().toString(),
    val mood : MoodEmoji,
    val note : String,
    val timeStamp : Long = System.currentTimeMillis()
)

enum class MoodEmoji(val emoji : String){
    RAD("😄"),
    GOOD("🙂"),
    MEH("😐"),
    BAD("🙁"),
    AWFUL("😢")
}