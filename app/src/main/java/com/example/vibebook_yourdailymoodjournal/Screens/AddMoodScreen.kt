@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("DEPRECATION")

package com.example.vibebook_yourdailymoodjournal.Screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibebook_yourdailymoodjournal.Data.MoodEmoji
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.example.vibebook_yourdailymoodjournal.ui.theme.DarkBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.DarkGreen
import com.example.vibebook_yourdailymoodjournal.ui.theme.MidBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.Orange
import com.example.vibebook_yourdailymoodjournal.ui.theme.SkyBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.SoftBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.fontFamily
import com.shashank.sony.fancytoastlib.FancyToast
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

//Add New Mood Screen
@SuppressLint("NewApi")
@Composable
fun AddMoods(navController: NavController, moodViewModel: MoodViewModel){

    var description by remember { mutableStateOf("") } //Note For Mood
    var selectedMood by remember { mutableStateOf<MoodEmoji?>(null) } //Select Mood Emoji
    var selectedDate: LocalDate? by remember { mutableStateOf<LocalDate?>(null) }//Selected Date
    var selectedTime: LocalTime? by remember { mutableStateOf(LocalTime.now()) } // Selected Time
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {

        //Add Mood Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            //Cancel Button
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                    Box( modifier = Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(vertical = 20.dp, horizontal = 10.dp)
                        .size(40.dp)
                        .clickable(onClick = { navController.popBackStack()}),
                    ){
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = Color.Red,
                            modifier = Modifier.align(Alignment.Center).size(30.dp))
                    }


                //Main Header
                Text(
                    text = "Hey! \uD83D\uDC4B How Are You?",
                    color = Color.Black,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically),
                    fontSize = 25.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            //DateTimePicker Function Calling
            DateTimePickerSection(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                onDateSelected = { selectedDate = it },
                onTimeSelected = { selectedTime = it }
            )

            Spacer(modifier = Modifier.height(10.dp))
            //Body
            Box(modifier = Modifier.fillMaxSize()) {

                //Emoji Selection Row
                MoodSelector(
                    selectedMood,
                    onMoodSelected = { mood -> selectedMood = mood },
                    description = description,
                    onDescriptionChange = { description = it },
                    selectedImageUris = selectedImageUris,
                    onImageUrisChanged = { newUris ->
                        selectedImageUris = newUris
                    }
                )
            }


            //Save Button
            Box(modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = {
                        if (selectedMood != null && description.isNotBlank()) {

                            val finalDate = selectedDate ?: LocalDate.now()
                            val finalTime = selectedTime ?: LocalTime.MIDNIGHT
                            val combinedDateTime = LocalDateTime.of(finalDate, finalTime)

                            val newMoodEntry = MoodEntry(
                                id = 0,
                                mood = selectedMood,
                                note = description,
                                dateTime = combinedDateTime.toString(),
                                imageList = selectedImageUris.map{it},
                            )

                            moodViewModel.addMoodEntry(newMoodEntry)
                            FancyToast.makeText(context, "Saved Successfully!",
                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show()
                            navController.popBackStack()
                        }
                        else{
                            FancyToast.makeText(context, "Please Fill Required Fields",
                                FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
                        }
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(10.dp)
                        .size(height = 50.dp, width = 130.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen,

                    )
                ) {
                    Text(text = "Save", fontSize = 20.sp, fontFamily = fontFamily)
                }
            }
        }
    }

}


//Mood Selector UI
@Composable
fun MoodSelector(selectedMood : MoodEmoji?,
                 onMoodSelected : (MoodEmoji) -> Unit,
                 description : String,
                 onDescriptionChange : (String) -> Unit,
                 selectedImageUris: List<Uri>,
                 onImageUrisChanged: (List<Uri>) -> Unit) {

    //For Enabling and disabling shadow of text field
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }
    var imageUri = remember { mutableStateListOf<Uri>()}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {//Detects tap gesture
                    focusManager.clearFocus()
                })
            }
            .padding(10.dp)
    ) {
        Column {
            //Emoji Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                //Calling Every Emoji From ENUM and Showing them in the row
                MoodEmoji.entries.forEach { mood ->
                    val isSelected =
                        mood == selectedMood //if mood == selectedMood then isSelected mood true
                    Column {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(
                                    //If particular emoji is selected then make its background Green and others Yellow
                                    if (isSelected) {
                                        when(mood.name){
                                            "Amazing" -> Color.Green
                                            "Good" -> SkyBlue
                                            "Okay" -> Color.Yellow
                                            "Bad" -> Orange
                                            "Terrible" -> Color.Red
                                            else -> Color.White
                                        }
                                    }else{
                                        Color.White
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
                                fontFamily = fontFamily
                            )
                        }
                        //Mood Text
                        Text(
                            text = mood.name,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .padding(vertical = 10.dp),
                            fontWeight = FontWeight.Medium,
                        )
                    }

                }
            }


            //Description TextField
            Text(
                text = "Note",
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                modifier = Modifier.padding(10.dp)
            )

            TextField(
                value = description, onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(100.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .shadow(
                        elevation = if (isFocused) 12.dp else 8.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, color = Color.White, RoundedCornerShape(10.dp)),
                    placeholder = { Text("Describe Your Mood",
                    fontFamily = fontFamily,
                    color = Color.White)},
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 3,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = fontFamily
                ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = MidBlue,
                        unfocusedContainerColor = DarkBlue,
                        focusedTextColor = Color.White,
                        ),
            )


            //Upload Photos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 30.dp)
            ) {
                val context = LocalContext.current


                //Gallery Launcher
                val galleryLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(2),
                    onResult = {uri ->
                        val remainingSlots = 2 - imageUri.size

                        val allowedUris = uri.take(remainingSlots)

                        if (remainingSlots <= 0) {
                            FancyToast.makeText(
                                context,
                                "You Already Added Two Images",
                                FancyToast.LENGTH_LONG,
                                FancyToast.WARNING,
                                true
                            ).show()
                            return@rememberLauncherForActivityResult
                        }

                        allowedUris.forEach { uri ->
                            try {
                                context.contentResolver.takePersistableUriPermission(
                                    uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )
                            } catch (e: SecurityException) {
                                FancyToast.makeText(
                                    context,
                                    e.printStackTrace().toString(),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.WARNING,
                                    true
                                ).show()
                            }
                        }
                        imageUri.addAll(allowedUris)
                        onImageUrisChanged(selectedImageUris + allowedUris)

                        if (uri.size > remainingSlots) {
                            FancyToast.makeText(
                                context,
                                "You can only add ${remainingSlots} more image(s)",
                                FancyToast.LENGTH_LONG,
                                FancyToast.WARNING,
                                true
                            ).show()
                        }
                    }
                )


                //Camera Launcher
                val cameraLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.TakePicture()
                ){ success ->
                        if(success && tempUri != null) {
                                if (imageUri.size < 2){
                                    tempUri?.let {
                                        imageUri.add(it)
                                        onImageUrisChanged(selectedImageUris + it)
                                    }
                                }
                            else{
                                    FancyToast.makeText(context,"You Already Added Two Images",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()
                                }
                            tempUri = null
                        }
                    else{
                            FancyToast.makeText(context,"Canceled",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()
                        }
                }

                //Header Text
                Column {
                    Text(
                        text = "Add Photos",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //Upload From Camera Card
                            Card(
                                modifier = Modifier
                                    .weight(0.5f).clickable(onClick = {

                                    val uri = createImageUri(context)
                                    tempUri = uri
                                    uri?.let {
                                        cameraLauncher.launch(it)
                                    }
                                }),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(SoftBlue)
                            ) {
                                Row(
                                    modifier = Modifier.padding(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.CameraAlt,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(35.dp)
                                            .padding(start = 5.dp)
                                            .align(alignment = Alignment.CenterVertically)
                                    )
                                    Text(
                                        "Camera",
                                        fontSize = 20.sp,
                                        fontFamily = fontFamily,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                            .align(alignment = Alignment.CenterVertically),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }


                            Spacer(modifier = Modifier.width(20.dp))
                            // Add space between cards

                        //Upload From Gallery Card
                            Card(
                                modifier = Modifier
                                    .weight(0.5f).clickable(onClick = {
                                    galleryLauncher.launch(PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                        colors = CardDefaults.cardColors(SoftBlue)
                            ) {
                                Row(
                                    modifier = Modifier.padding(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.FileUpload,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(35.dp)
                                            .padding(start = 5.dp)
                                            .align(alignment = Alignment.CenterVertically)
                                    )
                                    Text(
                                        "Upload",
                                        fontSize = 20.sp,
                                        fontFamily = fontFamily,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                            .padding(start = 5.dp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
               imageUri.forEach { image ->
                   ShowImageFromGallery(image)
               }
            }
        }
    }
}


//Date Time Picker
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePickerSection(
    selectedDate: LocalDate?,
    selectedTime: LocalTime?,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit
){
    val context = LocalContext.current
    var today = LocalDate.now()

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        //Date Picker Text
            //Date Selector Button
            Card(modifier = Modifier.padding(horizontal = 10.dp)
                .height(90.dp)
                .width(145.dp)
                .weight(0.5f),
                onClick = {
                val datePicker = DatePickerDialog(
                    context,
                    { _, dayOfMonth, month, year ->
                        val pickedDate = LocalDate.of(dayOfMonth, month + 1, year)
                        onDateSelected(pickedDate)
                    },
                    today.year,
                    today.monthValue - 1,
                    today.dayOfMonth
                )
                    datePicker.datePicker.maxDate = System.currentTimeMillis()
                    datePicker.show()
            },
                colors = CardDefaults.cardColors(
                    containerColor = DarkBlue
                ),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Date",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                            .padding(top = 20.dp),
                        fontSize = 17.sp,
                        color = Color.White,
                        fontFamily = fontFamily
                    )

                    val currentDate = LocalDate.now()

                    Text(
                        text = if (selectedDate != null) {
                            today = selectedDate
                            "${selectedDate.dayOfMonth}/${selectedDate.month}/${selectedDate.year} "
                        } else {
                            "$currentDate"
                        }, modifier = Modifier.padding(top = 10.dp, start = 3.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        fontSize = 17.sp,
                        fontFamily = fontFamily,
                        color = Color.White
                    )
                }
        }


            //Time Selector Button
            Card(modifier = Modifier.padding(horizontal = 10.dp)
                .height(90.dp)
                .width(200.dp)
                .weight(0.5f),onClick = {
                val now = LocalTime.now()
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val pickedTime = LocalTime.of(hour, minute)
                        if(pickedTime <= now){
                            onTimeSelected(pickedTime)
                        }
                        else
                        {
                            FancyToast.makeText(context, "Select Correct Time", FancyToast.LENGTH_SHORT,
                                FancyToast.WARNING, true).show()
                        }
                    },
                    now.hour,
                    now.minute,
                    false
                ).show()
            },
                colors = CardDefaults.cardColors(
                    containerColor = DarkBlue
                ),
                elevation = CardDefaults.cardElevation(10.dp),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

                    Text(
                        text = "Time",
                        Modifier.align(alignment = Alignment.CenterHorizontally)
                            .padding(top = 20.dp),
                        color = Color.White,
                        fontSize = 17.sp,
                        fontFamily = fontFamily
                    )

                    Text(

                        text = if (selectedTime != null) {
                            selectedTime.format(formatter)
                        } else {
                            LocalTime.now().format(formatter)
                        },
                        modifier = Modifier.padding(top = 10.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        fontSize = 17.sp,
                        fontFamily = fontFamily,
                        color = Color.White
                    )
                }
        }

    }
}


//Create Image Uri on Camera Button Click
fun createImageUri(context: Context) : Uri? {

    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
    }

    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

//Show Image Preview
@Composable fun ShowImageFromGallery(uri: Uri){
    Card(elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(10.dp)){
        AsyncImage(
            model = uri,
            contentDescription = "Uploaded From Gallery",
            modifier = Modifier
                .size(130.dp)
                .fillMaxWidth(),
        )
    }
}