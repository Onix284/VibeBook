package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel

@Composable
fun QuotesScreen(moodViewModel: MoodViewModel) {

    val quotes by moodViewModel.quotes.collectAsState()
    val quote = quotes.firstOrNull()
    val state by moodViewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {

            Card(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .height(350.dp)
                    .width(400.dp)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            ) {
               when {
                   state.isLoading -> {
                       Text(
                           "Loading...",
                           modifier = Modifier
                               .padding(50.dp)
                               .align(alignment = Alignment.CenterHorizontally)
                       )
                   }

                   !state.error.isNullOrEmpty() -> {
                       Text(
                           state.error.toString(),
                           modifier = Modifier
                               .padding(50.dp)
                               .align(alignment = Alignment.CenterHorizontally)
                       )
                   }

                   quote != null -> {
                       Text(
                           "\"${quote.q}\"\n\n— ${quote.a}",
                           modifier = Modifier
                               .padding(50.dp)
                               .align(Alignment.CenterHorizontally)
                       )
                   }

                   else -> {
                       Text(
                           "Tap refresh button to get a quote.",
                           modifier = Modifier
                               .padding(50.dp)
                               .align(Alignment.CenterHorizontally)
                       )
                   }

               }
            }


            Button(
                onClick = { moodViewModel.getQuote().toString() },
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                ),
                colors = ButtonDefaults.buttonColors(Color.DarkGray)
            ) {
                Icon(
                    imageVector = Icons.Default.Cached,
                    contentDescription = null
                )
            }
        }
    }
}
