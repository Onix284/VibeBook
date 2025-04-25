package com.example.vibebook_yourdailymoodjournal.Screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel

@Composable
fun MoodList(navController: NavController, moodViewModel: MoodViewModel){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("AddMood") }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        // Main content of the screen
        Column(modifier = Modifier.padding(it)) {

           Text(text = "This is Mood List Screen")
        }
    }

}

