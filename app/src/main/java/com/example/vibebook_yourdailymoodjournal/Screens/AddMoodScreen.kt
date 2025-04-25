@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.vibebook_yourdailymoodjournal.Screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.vibebook_yourdailymoodjournal.Data.MoodEmoji
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import java.time.LocalDate
import java.util.Calendar


@SuppressLint("NewApi")
@Composable
fun AddMoods(navController: NavController, moodViewModel: MoodViewModel){

    var description by remember { mutableStateOf("") } //Note For Mood
    var selectedMood by remember { mutableStateOf<MoodEmoji?>(null) } //Select Mood Emoji
    val emojis = listOf("😊", "😐", "😞", "😡", "😭") //List of Emoji

    //Date Picker
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate by remember { mutableStateOf("") }

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            },
            year, month, day
        )
    }

    //Add Mood Screen
    Column(modifier = Modifier.fillMaxSize()){

        //Header
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly){

            //Cancel Button
            Icon(Icons.Default.Close,
                contentDescription = "Cancel",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp).size(35.dp)
                    .background(color = androidx.compose.ui.graphics.Color.Transparent,
                        shape = RoundedCornerShape(16.dp))
                    .clickable(onClick = { navController.navigate("MoodList") }))

            //Main Header
            Text("Describe Your Mood",
                modifier = Modifier
                    .padding(vertical = 55.dp)
                    .height(30.dp),
                fontFamily = FontFamily.SansSerif,
                fontSize = 25.sp)

            //Calender Button
            Icon(Icons.Default.DateRange,
                contentDescription = "Cancel",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp).size(35.dp)
                    .background(color = androidx.compose.ui.graphics.Color.Transparent,
                        shape = RoundedCornerShape(16.dp))
                    .clickable(onClick = { datePickerDialog.show() }))
        }

        //Body
        Box (modifier = Modifier.fillMaxSize()){

            //Emoji Selection
            MoodSelector(selectedMood, onMoodSelected = {mood -> selectedMood = mood})


            //Save Button
            Icon(Icons.Default.Done,
                contentDescription = "Save Button",
                modifier = Modifier.padding(50.dp).size(70.dp)
                    .align(alignment = Alignment.BottomCenter)
                    .background(color = androidx.compose.ui.graphics.Color.Transparent,
                        shape = RoundedCornerShape(50.dp))
                )

            
        }
    }
}



@Composable
fun MoodSelector(selectedMood : MoodEmoji?, onMoodSelected : (MoodEmoji) -> Unit)
{
    Column {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly){
            MoodEmoji.entries.forEach {
                    mood ->
                val isSelected = mood == selectedMood
                Column {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) _root_ide_package_.androidx.compose.ui.graphics.Color.Green
                                else androidx.compose.ui.graphics.Color.Yellow
                            )
                            .clickable { onMoodSelected(mood) },
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = mood.emoji,
                            fontSize = 32.sp,
                        )
                    }
                    Text(
                        text = mood.name,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Text("Description")
        Text("Upload Image")
        Text("Voice Note")
    }
}