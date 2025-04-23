package com.example.vibebook_yourdailymoodjournal.Screens

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddMoods(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){

           Icon(Icons.AutoMirrored.Filled.ArrowBack,
               contentDescription = "Back To Home Screen Button",
               modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp).size(35.dp)
                   .background(color = androidx.compose.ui.graphics.Color.Transparent,
                                shape = RoundedCornerShape(16.dp))
                   .clickable(onClick = { navController.navigate("MoodList") }))



        Text("Add Mood Screen", modifier = Modifier.align(Alignment.Center))
    }
}
