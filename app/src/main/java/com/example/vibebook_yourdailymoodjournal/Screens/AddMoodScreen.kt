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
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibebook_yourdailymoodjournal.Data.MoodEmoji
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.shashank.sony.fancytoastlib.FancyToast
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
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {

        //Add Mood Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                //Cancel Button
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cancel",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(20.dp)
                        .size(35.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = { navController.popBackStack()})
                )

                //Main Header
                Text(
                    "Describe Your Mood",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 25.sp,

                )
            }

            //DateTimePicker Function Calling
            DateTimePickerSection(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                onDateSelected = { selectedDate = it },
                onTimeSelected = { selectedTime = it }
            )

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
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 0.dp)){
            //Save Button
            FloatingActionButton(
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
                        navController.popBackStack()
                    }
                    else{
                        FancyToast.makeText(context, "Please Fill Required Fields",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
                    }
                },
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(bottom = 50.dp)
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save Button")
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
            Spacer(modifier = Modifier.height(20.dp))

            //Description TextField
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
                        elevation = if (isFocused) 8.dp else 0.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, color = Color.Blue, RoundedCornerShape(10.dp)),
                placeholder = { Text("Enter About Your Mood") },
                shape = RoundedCornerShape(10.dp),
                maxLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
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
                    contract = ActivityResultContracts.PickMultipleVisualMedia(4),
                    onResult = {uri ->
                        if(imageUri.size < 4 ) {
                                uri.let {
                                    imageUri.addAll(it)


                                    it.forEach {
                                        try{
                                            context.contentResolver.takePersistableUriPermission(
                                                it,
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            )
                                        }catch (e : SecurityException){
                                            FancyToast.makeText(
                                                context,
                                                e.printStackTrace().toString(),
                                                FancyToast.LENGTH_LONG,
                                                FancyToast.WARNING,
                                                true
                                            ).show()
                                        }
                                    }

                                    onImageUrisChanged(selectedImageUris + it)

                                    if (imageUri.size > 4) {
                                        FancyToast.makeText(
                                            context,
                                            "You Can Add Only Upto 4 Images",
                                            FancyToast.LENGTH_LONG,
                                            FancyToast.WARNING,
                                            true
                                        ).show()
                                    }
                            }
                        }
                        else
                        {
                            FancyToast.makeText(
                                context,
                                "You Already Added Four Images",
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
                                if (imageUri.size < 4){
                                    tempUri?.let {
                                        imageUri.add(it)
                                        onImageUrisChanged(selectedImageUris + it)
                                    }
                                }
                            else{
                                    FancyToast.makeText(context,"You Already Added Four Images",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()
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
                        text = "Photos",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 25.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //Upload From Camera Card
                            Card(
                                modifier = Modifier.clickable(onClick = {
                                    val uri = createImageUri(context)
                                    tempUri = uri
                                    uri?.let {
                                        cameraLauncher.launch(it)
                                    }
                                }),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                                        fontFamily = FontFamily.SansSerif,
                                        modifier = Modifier
                                            .padding(horizontal = 15.dp, vertical = 10.dp)
                                            .align(alignment = Alignment.CenterVertically),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }


                            Spacer(modifier = Modifier.width(20.dp))
                            // Add space between cards

                            //Upload From Gallery Card
                            Card(
                                modifier = Modifier.clickable(onClick = {
                                    galleryLauncher.launch(PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                                        fontFamily = FontFamily.SansSerif,
                                        modifier = Modifier
                                            .padding(horizontal = 15.dp, vertical = 10.dp)
                                            .padding(start = 5.dp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
            }
            LazyRow(modifier = Modifier.fillMaxHeight()) {
               item {imageUri.forEach { image ->
                   ShowImageFromGallery(image)
               }
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        //Date Picker Text
        Column {
            //Date Selector Button
            Button(onClick = {
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
                    text = "Select Date"
                )
            }

            val currentDate = LocalDate.now()

            Text(
                text = if(selectedDate != null){
                    today = selectedDate
                     "${selectedDate.dayOfMonth}/${selectedDate.month}/${selectedDate.year} "
                }
                else{
                    "$currentDate"
                }, modifier = Modifier.padding(vertical = 20.dp),
                fontSize = 17.sp,
                fontFamily = FontFamily.Monospace
            )
        }


        //Time Picker Text
        Column {
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
                    text = "Select Time"
                )
            }
            Text(
                text =  if(selectedTime != null){
                    "$selectedTime"
                }
                else{
                    "${LocalTime.now().hour}:${LocalTime.now().minute}"
                },
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp),
                fontSize = 17.sp,
                fontFamily = FontFamily.Monospace,
            )
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