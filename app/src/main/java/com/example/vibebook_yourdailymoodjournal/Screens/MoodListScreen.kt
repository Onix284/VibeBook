package com.example.vibebook_yourdailymoodjournal.Screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel

@Composable
fun MoodList(navController: NavController, moodViewModel: MoodViewModel){

    val moodEntries by moodViewModel.moodEntries.collectAsState() //Public moodEntries variable from ViewModel

    //Group Mood Entries According to date int descending order
    val groupedMoods = moodEntries
                        .sortedByDescending { it.dateTime }
                        .groupBy { it.dateTime?.substringBefore('T') } //substring before T will only include Date removes Time

    Scaffold(
        //Add New Mood Button
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("AddMood"){
                launchSingleTop = true
                restoreState = true
            } },
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }, //Floating Action Button Position
    ) {

        // Main content of the screen (List Of Moods)
        LazyColumn(modifier = Modifier.padding(it)) {
            //Getting every value of group of moods, grouped by date
            groupedMoods.forEach{ (date, moods) ->
                item{
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 15.dp)
                        .clickable(onClick = {navController.navigate("ViewMood"){
                            launchSingleTop = true
                            restoreState = true
                        } })
                    ){
                        Column(modifier = Modifier.padding(16.dp))
                        {

                            //Reverse Date Format
                            val formattedDate = date.let {
                                val parts = it?.split("-")
                                if(parts?.size==3){
                                    "${parts[2]}-${parts[1]}-${parts[0]}"
                                }
                                else
                                {
                                    it
                                }
                            }
                            //Header Date
                            Text(
                                text = formattedDate.toString(),
                                modifier = Modifier.padding(bottom = 8.dp),
                                fontWeight = FontWeight.Medium
                            )

                            //Passing the mood data to MoodEntryItem Function
                            moods.forEach {
                                moodEntry ->
                                MoodEntryItem(
                                    myMoodEntry = moodEntry,
                                    onDeleteClick = {moodViewModel.deleteMood(moodEntry)}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//Shows the data of mood
@Composable
fun MoodEntryItem(myMoodEntry: MoodEntry, onDeleteClick : (MoodEntry) -> Unit ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 4.dp, horizontal = 0.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Row {
                    Column{
                        Text("${myMoodEntry.mood?.emoji} ${myMoodEntry.mood?.name}", fontSize = 35.sp) //Mood Name
                        Text("Time: ${myMoodEntry.dateTime?.substringAfter('T')}") //Mood Time
                        myMoodEntry.imageList.forEach {
                                AsyncImage(
                                    model = it,
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                )
                        }
                    }

                }
            }
            //Delete Mood Button
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .size(40.dp)
                    .align(alignment = Alignment.CenterEnd)
                    .clickable{
                        onDeleteClick(myMoodEntry)
                    })
        }
    }
}