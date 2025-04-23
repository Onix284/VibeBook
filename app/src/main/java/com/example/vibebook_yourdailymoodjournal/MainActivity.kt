package com.example.vibebook_yourdailymoodjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vibebook_yourdailymoodjournal.Screens.AddMoods
import com.example.vibebook_yourdailymoodjournal.Screens.MoodList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "MoodList", builder = {
                composable("MoodList" ) {
                    MoodList(navController)
                }
                composable("AddMood" ) {
                    AddMoods(navController)
                }
            })
        }
    }
}

