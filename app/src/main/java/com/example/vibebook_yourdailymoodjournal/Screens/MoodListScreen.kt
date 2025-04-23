package com.example.vibebook_yourdailymoodjournal.Screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController

@Composable
fun MoodList(name : String, modifier: Modifier){
    var navController : NavController
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {  },
                                modifier = Modifier.padding(vertical = 25.dp, horizontal = 10.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        // Main content of the screen
        Column(modifier = Modifier.padding(it)) {

           Text(text = "This is ${name}")
        }
    }

}

