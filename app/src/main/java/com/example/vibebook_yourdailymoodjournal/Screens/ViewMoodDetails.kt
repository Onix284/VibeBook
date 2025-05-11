package com.example.vibebook_yourdailymoodjournal.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.example.vibebook_yourdailymoodjournal.ui.theme.DarkBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.Orange
import com.example.vibebook_yourdailymoodjournal.ui.theme.SkyBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.fontFamily
import kotlinx.coroutines.flow.Flow

@SuppressLint("SuspiciousIndentation")
@Composable
fun ViewMoodDetails(navController: NavController, moodViewModel: MoodViewModel, id : Int){

    val moodEntryFlow : Flow<MoodEntry?> = moodViewModel.getMood(id)
    val moodEntry by moodEntryFlow.collectAsState(initial = null)

    Column(modifier = Modifier.fillMaxSize()
    ){
        Row{
             //Header
             Box(modifier = Modifier.fillMaxWidth()
             ){
             Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                    .align(alignment = Alignment.TopStart)
                    .padding(start = 15.dp)
                    .clickable(onClick = {navController.popBackStack()})
                    )

                    Text("You Mood \uD83D\uDE0E",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 25.sp,
                    fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold)
               }

             }

            //Content
             Box(modifier = Modifier.fillMaxSize()){

                 Card(Modifier.padding(horizontal = 20.dp)
                     .heightIn(min = 700.dp),
                     colors = CardDefaults.cardColors(when(moodEntry?.mood?.name){
                         "Amazing" -> Color.Green.copy(alpha = 0.3f)
                         "Good" -> SkyBlue.copy(alpha = 0.3f)
                         "Okay" -> Color.Yellow.copy(alpha = 0.3f)
                         "Bad" -> Orange.copy(alpha = 0.3f)
                         "Terrible" -> Color.Red.copy(alpha = 0.3f)
                         else -> Color.White.copy(alpha = 0.3f)
                     })){

                     LazyColumn(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                         .padding(top = 40.dp)
                     ){
                         item {
                             Box(modifier = Modifier.fillMaxWidth()){
                                 //Mood Name
                                 Text(text = moodEntry?.mood?.name ?: "Unknown",
                                     modifier = Modifier.align(alignment = Alignment.Center),
                                     fontSize = 40.sp,
                                     fontFamily = fontFamily)
                             }
                         }

                         item {
                             //Mood Emoji
                             Box(modifier = Modifier.fillMaxWidth()) {
                                 Text(
                                     text = moodEntry?.mood?.emoji ?: "Unknown", fontSize = 100.sp,
                                     modifier = Modifier.align(alignment = Alignment.Center)
                                         .shadow(
                                             elevation = 20.dp,
                                             shape = RoundedCornerShape(100.dp),
                                         )
                                 )
                             }
                         }

                         item {
                             Spacer(modifier = Modifier.height(10.dp))
                         }

                         item {
                             //Images
                             val imageList = moodEntry?.imageList ?: emptyList()

                             if(imageList.size == 1){
                                 Row(modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(horizontal = 10.dp),
                                     horizontalArrangement = Arrangement.Center
                                 ){
                                     Card {
                                         AsyncImage(
                                             model = imageList[0],
                                             contentDescription = null,
                                             modifier = Modifier.fillMaxSize(),
                                             contentScale = ContentScale.Fit
                                         )
                                     }
                                 }
                             }

                             else if (imageList.size > 1){
                                 Row(
                                     modifier = Modifier
                                         .fillMaxWidth(),
                                     horizontalArrangement = Arrangement.spacedBy(0.dp)
                                 ){
                                     moodEntry?.imageList?.forEach {
                                             images ->
                                         Card(modifier = Modifier
                                             .size(160.dp)
                                             .clip(RoundedCornerShape(16.dp)),
                                             shape = RoundedCornerShape(16.dp),
                                             colors = CardDefaults.cardColors(Color.Transparent)
                                         ) {
                                             AsyncImage(
                                                 model = images,
                                                 contentDescription = null,
                                                 modifier = Modifier.fillMaxSize(),
                                                 contentScale = ContentScale.Fit
                                             )
                                         }

                                     }
                                 }
                             }
                         }

                         item {
                             //Note
                             Card(modifier = Modifier.fillMaxWidth()
                                 .align(alignment = Alignment.CenterHorizontally)
                                 .padding(horizontal = 30.dp, vertical = 20.dp)
                                 .defaultMinSize(minHeight = 100.dp),
                                 colors = CardDefaults.cardColors(DarkBlue)) {

                                 Text(text = moodEntry?.note.toString(),
                                     modifier = Modifier.padding(15.dp),
                                     fontFamily = fontFamily,
                                     fontSize = 18.sp,
                                     color = Color.White)
                             }
                         }
                     }
                 }

             }
       }
}