package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FilterFrames
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route : String, val title : String, val icon : ImageVector) {
    object MoodList : Screen("MoodList", "Home", Icons.Default.Home)
    object MoodStats : Screen("MoodStats", "Stats", Icons.Default.BarChart)
    object QuotesScreen : Screen("QuotesScreen", "Quotes", Icons.Default.FilterFrames)

    companion object{
        val bottonNavItems = listOf(MoodList, MoodStats, QuotesScreen)
    }
}