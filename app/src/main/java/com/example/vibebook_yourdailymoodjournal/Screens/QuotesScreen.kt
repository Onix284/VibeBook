package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuotesScreen(){
    Box(modifier = Modifier.fillMaxSize()){
        Text("This is QuotesScreen", modifier = Modifier.align(Alignment.Center))
    }
}