package com.example.vibebook_yourdailymoodjournal

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vibebook_yourdailymoodjournal.Data.MoodDatabase
import com.example.vibebook_yourdailymoodjournal.Screens.AddMoods
import com.example.vibebook_yourdailymoodjournal.Screens.MoodList
import com.example.vibebook_yourdailymoodjournal.Screens.MoodStats
import com.example.vibebook_yourdailymoodjournal.Screens.QuotesScreen
import com.example.vibebook_yourdailymoodjournal.Screens.Screen
import com.example.vibebook_yourdailymoodjournal.Screens.ViewMoodDetails
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.example.vibebook_yourdailymoodjournal.ui.theme.fontFamily

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val moodDatabase = MoodDatabase.getDatabase(applicationContext)
        val moodViewModel = MoodViewModel(moodDatabase.moodDao())

     setContent {
         val navController = rememberNavController()
         val currentBackStack by navController.currentBackStackEntryAsState()
         val currentDestination = currentBackStack?.destination?.route

             Scaffold(
                 bottomBar = {
                         BottomNavigation(elevation = 8.dp,
                             modifier = Modifier.navigationBarsPadding(),
                             backgroundColor = Color.White,
                             contentColor = Color.Black) {
                             Screen.bottonNavItems.forEach { screen ->
                                     BottomNavigationItem(
                                         icon = {
                                             Icon(
                                                 screen.icon,
                                                 contentDescription = screen.title
                                             )
                                         },
                                         label = { Text(text = screen.title,
                                             fontFamily = fontFamily,
                                             color = Color.Black,
                                             modifier = Modifier.padding(top = 5.dp))
                                                 },
                                         modifier = Modifier.align(alignment = Alignment.CenterVertically)
                                             .padding(10.dp),
                                         selected = currentDestination == screen.route,
                                         onClick = {
                                             if (currentDestination != screen.route) {
                                                 navController.navigate(screen.route) {
                                                     popUpTo(navController.graph.startDestinationId) {
                                                         saveState = true
                                                     }
                                                     launchSingleTop = true
                                                     restoreState = true
                                                 }
                                             }
                                         }
                                     )
                                 }
                             }
                 }
             ) { innerPadding ->
                 NavHost(
                     navController = navController,
                     startDestination = Screen.MoodList.route,
                     modifier = Modifier.padding(innerPadding)
                 ) {
                     composable(Screen.MoodList.route) {
                         MoodList(navController, moodViewModel)
                     }
                     composable(Screen.MoodStats.route) {
                         MoodStats(moodViewModel)
                     }
                     composable(Screen.QuotesScreen.route) {
                         QuotesScreen()
                     }

                     // These are internal routes not in bottom nav
                     composable("AddMood") {
                         AddMoods(navController, moodViewModel)
                     }
                     composable("ViewMood/{id}") { backStackEntry ->
                         val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                         if(id != null){
                             ViewMoodDetails(navController, moodViewModel, id)
                         }

                     }
                 }
             }
         }
    }
}