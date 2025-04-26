package com.example.vibebook_yourdailymoodjournal.Screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel

@Composable
fun MoodList(navController: NavController, moodViewModel: MoodViewModel){
    val moodEntries by moodViewModel.moodEntries.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("AddMood") },
                modifier = Modifier.padding(vertical = 30.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        // Main content of the screen
        LazyColumn(modifier = Modifier.padding(it)) {
            items(moodEntries) { moodEntry ->
                MoodEntryItem(myMoodEntry = moodEntry)
            }
        }
    }

}

@Composable
fun MoodEntryItem(myMoodEntry: MoodEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 15.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mood: ${myMoodEntry.mood?.name}")
            Text("Description ${myMoodEntry.note}")
            Text("Date: ${myMoodEntry.dateTime}")

        }
    }
}