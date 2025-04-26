@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.vibebook_yourdailymoodjournal.Screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibebook_yourdailymoodjournal.Data.MoodEmoji
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

//Add New Mood Screen
@SuppressLint("NewApi")
@Composable
fun AddMoods(navController: NavController, moodViewModel: MoodViewModel){

    var description by remember { mutableStateOf("") } //Note For Mood
    var selectedMood by remember { mutableStateOf<MoodEmoji?>(null) } //Select Mood Emoji

    var selectedDate: LocalDate? by remember { mutableStateOf<LocalDate?>(null) }//Selected Date
    var selectedTime: LocalTime? by remember { mutableStateOf<LocalTime?>(null) } // Selected Time

    //Add Mood Screen
    Column(modifier = Modifier.fillMaxSize()){

        //Header
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start){

            //Cancel Button
            Icon(Icons.Default.Close,
                contentDescription = "Cancel",
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 50.dp)
                    .size(35.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(onClick = { navController.navigate("MoodList") }))

            //Main Header
            Text("Describe Your Mood",
                modifier = Modifier
                    .padding(vertical = 53.dp, horizontal = 20.dp)
                    .height(30.dp),
                fontFamily = FontFamily.SansSerif,
                fontSize = 25.sp)
        }

        //DateTimePicker Function Calling
        DateTimePickerSection(
            selectedDate = selectedDate,
            selectedTime = selectedTime,
            onDateSelected = { selectedDate = it },
            onTimeSelected = { selectedTime = it }
        )

        //Body
        Box (modifier = Modifier.fillMaxSize()){

            //Emoji Selection Row
            MoodSelector(selectedMood,
                onMoodSelected = {mood -> selectedMood = mood},
                description = description,
                onDescriptionChange = {description = it}
            )


            //Save Button
            FloatingActionButton(onClick = {
                if(selectedMood != null && description.isNotBlank()){

                    val finalDate = selectedDate ?: LocalDate.now()
                    val finalTime = selectedTime ?: LocalTime.MIDNIGHT
                    val combinedDateTime = LocalDateTime.of(finalDate, finalTime)
                        val newMoodEntry = MoodEntry(
                            id = 0,
                            mood = selectedMood,
                            note = description,
                            dateTime = combinedDateTime.toString()
                        )
                        moodViewModel.addMoodEntry(newMoodEntry)
                }
                navController.navigate("MoodList")},
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
                    .padding(vertical = 50.dp)){
               Icon(Icons.Default.Done, contentDescription= "Save Button")
            }
        }
    }
}


//Mood Selector UI
@Composable
fun MoodSelector(selectedMood : MoodEmoji?,
                 onMoodSelected : (MoodEmoji) -> Unit,
                 description : String,
                 onDescriptionChange : (String) -> Unit)
{
    //For Enabling and disabling shadow of text field
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {//Detects tap gesture
                focusManager.clearFocus()
            })
        }
        .padding(10.dp)
    ){
    Column {
        //Emoji Row
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly){
            //Calling Every Emoji From ENUM and Showing them in the row
            MoodEmoji.entries.forEach {
                    mood ->
                val isSelected = mood == selectedMood //if mood == selectedMood then isSelected mood true
                Column {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                //If particular emoji is selected then make its background Green and others Yellow
                                if (isSelected) {
                                    Color.Green
                                } else {
                                    Color.Yellow
                                }

                            )
                            //Onclick Mood
                            .clickable { onMoodSelected(mood) },
                        contentAlignment = Alignment.Center
                    ) {
                        //Mood Emoji
                        Text(
                            text = mood.emoji,
                            fontSize = 32.sp,
                        )
                    }
                    //Mood Text
                    Text(
                        text = mood.name,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(40.dp))

            //Description TextField
            TextField(value = description, onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(100.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .shadow(
                        elevation = if (isFocused) 8.dp else 0.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, color = Color.Blue, RoundedCornerShape(10.dp)),
                placeholder = { Text("Enter About Your Mood")},
                shape = RoundedCornerShape(10.dp),
                maxLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ))
        }
    }
}

//Date App Picker
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePickerSection(
    selectedDate: LocalDate?,
    selectedTime: LocalTime?,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit
){
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        //Date Selector Button
        Button(onClick = {
            val today = LocalDate.now()
            DatePickerDialog(
                context,
                { _, dayOfMonth, month, year ->
                    val pickedDate = LocalDate.of(dayOfMonth, month + 1, year)
                    onDateSelected(pickedDate)
                },
                today.year,
                today.monthValue - 1,
                today.dayOfMonth
            ).show()
        }){
            Text(
                text = selectedDate?.toString() ?: "Select Date"
            )
        }

        //Time Selector Button
        Button(onClick = {
            val now = LocalTime.now()
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    val pickedTime = LocalTime.of(hour, minute)
                    onTimeSelected(pickedTime)
                },
                now.hour,
                now.minute,
                false
            ).show()
        }) {
            Text(
                text = selectedTime?.toString() ?: "Select Time"
            )
        }
    }
}