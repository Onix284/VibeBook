package com.example.vibebook_yourdailymoodjournal.Screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.example.vibebook_yourdailymoodjournal.ui.theme.DarkBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.DarkGry
import com.example.vibebook_yourdailymoodjournal.ui.theme.Orange
import com.example.vibebook_yourdailymoodjournal.ui.theme.SkyBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.fontFamily
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
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

        //Header
        Box (modifier = Modifier.fillMaxWidth()
            .padding(10.dp)){
            Text(
                text = "Mood Journal \uD83D\uDCC5",
                color = Color.Black,
                modifier = Modifier
                    .align(alignment = Alignment.Center),
                fontSize = 25.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Main content of the screen (List Of Moods)
        LazyColumn(modifier = Modifier.padding(it).padding(top = 20.dp)) {
            //Getting every value of group of moods, grouped by date
            groupedMoods.forEach{ (date, moods) ->
                item{
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 15.dp),
                        colors = CardDefaults.cardColors(
                            DarkBlue
                        )
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
                                modifier = Modifier.padding(bottom = 0.dp),
                                fontWeight = FontWeight.Medium,
                                fontFamily = fontFamily,
                                color = Color.White
                            )

                            //Passing the mood data to MoodEntryItem Function
                            moods.forEach {
                                moodEntry ->
                                MoodEntryItem(
                                    myMoodEntry = moodEntry,
                                    onDeleteClick = {moodViewModel.deleteMood(moodEntry)},
                                    navController = navController
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoodEntryItem(myMoodEntry: MoodEntry, onDeleteClick : (MoodEntry) -> Unit, navController : NavController ) {

    Card(modifier = Modifier.padding(5.dp)
        .clip(shape = RoundedCornerShape(15.dp))
        .clickable(onClick = {navController.navigate("ViewMood/${myMoodEntry.id}"){
        launchSingleTop = true
        restoreState = true
    } }),
        colors = CardDefaults.cardColors(DarkBlue),
        ){
        Box(modifier = Modifier.fillMaxSize().size(90.dp)) {
            Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp).fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically)
                {

                    Text(
                        "${myMoodEntry.mood?.emoji}",
                        fontSize = 45.sp,
                        fontFamily = fontFamily
                    )

                    //Mood Name
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp))
                    {
                        Text(
                            "${myMoodEntry.mood?.name}",
                            fontSize = 25.sp,
                            fontFamily = fontFamily,
                            color = when (myMoodEntry.mood?.name) {
                                "Amazing" -> Color.Green
                                "Good" -> SkyBlue
                                "Okay" -> Color.Yellow
                                "Bad" -> Orange
                                "Terrible" -> Color.Red
                                else -> Color.White
                            },
                        )

                        val inputDateTime = myMoodEntry.dateTime // e.g., "2025-05-03T13:45:00"
                        val parsedTime = LocalTime.parse(inputDateTime?.substringAfter('T'))
                        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

                        Text(
                            "${parsedTime.format(formatter)}",
                            fontFamily = fontFamily,
                            color = Color.White
                        )
                    }
                }
            }
            Box (modifier = Modifier.fillMaxSize()) {
                //Delete Mood Button
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete button",
                    modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 10.dp)
                        .size(35.dp)
                        .align(alignment = Alignment.CenterEnd)
                        .clickable {
                            onDeleteClick(myMoodEntry)
                        })
            }
        }
    }

}