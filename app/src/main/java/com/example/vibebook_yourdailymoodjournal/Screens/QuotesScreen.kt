package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel

@Composable
fun QuotesScreen(moodViewModel: MoodViewModel) {

    val quotes by moodViewModel.quotes.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        var mainText = quotes.firstOrNull() ?: "Tap a mood to see a quote"
        Card(modifier = Modifier
            .align(alignment = Alignment.Center)
            .height(350.dp)
            .width(400.dp)
            .clickable(onClick = {mainText = quotes.toString()})
            .padding(horizontal = 20.dp, vertical = 10.dp),
        ){
            Text(mainText, modifier = Modifier
                .padding(50.dp)
                .align(alignment = Alignment.CenterHorizontally))
        }
    }
}
