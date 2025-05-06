package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibebook_yourdailymoodjournal.Data.MoodEmoji
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.example.vibebook_yourdailymoodjournal.ui.theme.Orange
import com.example.vibebook_yourdailymoodjournal.ui.theme.SkyBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.fontFamily
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer


@Composable
fun MoodStats(moodViewModel: MoodViewModel){

    val moodEntry by moodViewModel.moodEntries.collectAsState()
    //Count each emoji occurrence
    val moodCounts = moodEntry.groupingBy { it.mood }.eachCount()

    //Map Each Mood to a specific color
    val moodColors = mapOf(
        MoodEmoji.Amazing to Color(0xFF81C784),
        MoodEmoji.Good to SkyBlue,
        MoodEmoji.Okay to Color.Yellow,
        MoodEmoji.Bad to Orange,
        MoodEmoji.Terrible to Color.Red
    )

    LazyColumn {


       item {

           Column(modifier = Modifier.fillMaxWidth()) {

               for(mood in MoodEmoji.entries){
                   Card(modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 100.dp, vertical = 5.dp)) {
                       Row (modifier = Modifier
                           .background(
                               color = when(mood.name){
                                   "Amazing" -> Color.Green // light green
                                   "Good" -> SkyBlue     // sky blue
                                   "Okay" -> Color.Yellow
                                   "Bad" -> Orange      // orange
                                   "Terrible" -> Color.Red
                                   else -> Color.Gray
                               }
                           )
                           .padding(5.dp)){
                           Text(mood.emoji, fontSize = 25.sp, fontFamily = fontFamily, modifier = Modifier.padding(start = 5.dp))
                           Column(modifier = Modifier.fillMaxWidth()){
                               Text(mood.name, fontSize = 25.sp, fontFamily = fontFamily, modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                           }
                       }
                   }
               }

               // Pie Chart
               val PieChartdata = PieChartData(
                   slices = moodCounts.entries
                       .filter { it.key != null }
                       .map { (mood, count) ->
                           PieChartData.Slice(
                               value = count.toFloat(),
                               color = moodColors[mood!!] ?: Color.Gray
                           )
                       }
               )



               Spacer(modifier = Modifier.height(30.dp))

               androidx.compose.material3.Card(modifier = Modifier
                   .padding(10.dp)
                   .height(50.dp)
                   .width(150.dp),
                   shape = RoundedCornerShape(8.dp),
                   elevation = CardDefaults.cardElevation(10.dp),
                   colors = CardDefaults.cardColors(Color.White)
               ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Pie Chart : ", fontSize = 20.sp, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold)
                    }
               }

               PieChart(
                   pieChartData = PieChartdata,
                   modifier = Modifier
                       .size(300.dp)
                       .align(Alignment.CenterHorizontally),
                   animation = simpleChartAnimation(),
                   sliceDrawer = SimpleSliceDrawer(),
               )
           }
       }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            // Bar Chart
            Box(modifier = Modifier
                .fillMaxWidth()){

                val BarChartdata = BarChartData(
                    bars = moodCounts.entries
                        .filter { it.key != null }
                        .map { (mood, count) ->
                            BarChartData.Bar(
                                value = count.toFloat(),
                                label = mood!!.emoji,
                                color = moodColors[mood] ?: Color.Gray
                            )
                        }
                )

                androidx.compose.material3.Card(modifier = Modifier
                    .padding(10.dp)
                    .height(50.dp)
                    .width(150.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Bar Chart : ", fontSize = 20.sp, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold)
                    }
                }


                BarChart(
                    barChartData = BarChartdata,
                    modifier = Modifier
                        .size(500.dp)
                        .padding(top = 50.dp)
                        .align(alignment = Alignment.Center),
                    animation = simpleChartAnimation(),
                    barDrawer = SimpleBarDrawer(),
                    xAxisDrawer = SimpleXAxisDrawer(),
                    yAxisDrawer = SimpleYAxisDrawer(),
                    labelDrawer = SimpleValueDrawer(
                        drawLocation = SimpleValueDrawer.DrawLocation.Outside,
                        labelTextSize = 30.sp
                    )
                )
            }
        }

        item {

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

