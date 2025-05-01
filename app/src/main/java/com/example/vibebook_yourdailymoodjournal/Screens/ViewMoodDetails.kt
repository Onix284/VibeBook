package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibebook_yourdailymoodjournal.Data.MoodEntry
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel

@Composable
fun ViewMoodDetails(navController: NavController, moodViewModel: MoodViewModel){

    val moodEntries by moodViewModel.moodEntries.collectAsState()

            Box(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(45.dp)){

                Text("You Vibe", modifier = Modifier.align(Alignment.Center))

                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = {navController.navigate("MoodList")})
                )

                moodEntries.forEach {
                    MainContent(it)
                }

            }


        }


@Composable
fun MainContent(moodEntry: MoodEntry){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        item {
            moodEntry.imageList.forEach {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }

}