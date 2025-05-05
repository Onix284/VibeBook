package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vibebook_yourdailymoodjournal.Data.MoodEmoji
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.example.vibebook_yourdailymoodjournal.ui.theme.Orange
import com.example.vibebook_yourdailymoodjournal.ui.theme.SkyBlue
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlin.collections.*


@Composable
fun MoodStats(moodViewModel: MoodViewModel){

    val moodEntry by moodViewModel.moodEntries.collectAsState()

    //Count each emoji occurance
    val moodCounts = moodEntry.groupingBy { it.mood }.eachCount()

    //Map Each Mood to a specific color
    val moodColors = mapOf(
        MoodEmoji.Amazing to Color(0xFF81C784),
        MoodEmoji.Good to SkyBlue,
        MoodEmoji.Okay to Color.Yellow,
        MoodEmoji.Bad to Orange,
        MoodEmoji.Terrible to Color.Red
    )



        val data = PieChartData(
            slices = moodCounts.entries
                .filter { it.key != null }
                .map { (mood, count) ->
                    PieChartData.Slice(
                        value = count.toFloat(),
                        color = moodColors[mood!!] ?: Color.Gray
                    )
                }
        )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize().align(Alignment.Center)){
            Row {
                Card(colors = CardDefaults.cardColors(Color.Red)){
                    val mood = moodEntry.groupingBy { it.mood?.emoji }


                }
            }
        }

        PieChart(
            pieChartData = data,
            modifier = Modifier.size(300.dp).align(Alignment.TopCenter),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer()
        )
    }
}

