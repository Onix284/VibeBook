package com.example.vibebook_yourdailymoodjournal.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vibebook_yourdailymoodjournal.ViewModel.MoodViewModel
import com.example.vibebook_yourdailymoodjournal.ui.theme.DarkBlue
import com.example.vibebook_yourdailymoodjournal.ui.theme.fontFamily
import kotlin.random.Random

//Quotes Screen
@Composable
fun QuotesScreen(moodViewModel: MoodViewModel) {

    val quotes by moodViewModel.quotes.collectAsState() //QuotesItem Response
    val quote = quotes.firstOrNull() //One Quote
    val state by moodViewModel.state.collectAsState() //Screen State
    val context = LocalContext.current

    //Background Images Array
    val BackgroundImageList = arrayOf(
        "https://i.pinimg.com/736x/ab/6d/70/ab6d70aa684f8262313dff2897398949.jpg",
        "https://images.unsplash.com/photo-1487147264018-f937fba0c817?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://i.pinimg.com/736x/fd/21/75/fd21751f8d78a999617a333541a50a44.jpg",
        "https://i.pinimg.com/736x/6d/b1/f2/6db1f2cb832a7e4959ea9f66f9443d0b.jpg",
        "https://i.pinimg.com/736x/f9/47/83/f9478331558b4b2f635e3adecfd259d9.jpg"
    )

    val image = remember { mutableStateOf(getRandomImage(BackgroundImageList))}

    //Body
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = DarkBlue),
        contentAlignment = Alignment.Center) {
        Column{

            //Main Card
            Card(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .width(400.dp)
                    .padding(horizontal = 20.dp)
                    .height(500.dp)
                    .padding(top = 10.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()){

                    //Background Image
                    AsyncImage(
                        model = image.value,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    //Check Screen State
                    when {
                        //Loading
                        state.isLoading -> {
                            Text(
                                "Loading...",
                                modifier = Modifier
                                    .padding(30.dp)
                                    .align(alignment = Alignment.Center),
                                fontSize = 30.sp,
                                fontFamily = fontFamily
                            )
                        }

                        //Error
                        !state.error.isNullOrEmpty() -> {
                            Text(
                                state.error.toString(),
                                modifier = Modifier
                                    .padding(30.dp)
                                    .align(alignment = Alignment.Center),
                                fontSize = 30.sp,
                                fontFamily = fontFamily
                            )
                        }

                        //Quote
                        quote != null -> {
                            Box(modifier = Modifier.fillMaxHeight()){
                                Text(
                                    "\"${quote.q}\"\n\n— ${quote.a}",
                                    modifier = Modifier
                                        .padding(30.dp)
                                        .align(Alignment.Center),
                                    fontSize = 30.sp,
                                    fontFamily = fontFamily
                                )
                            }
                        }

                        //Ideal
                        else -> {
                            Text(
                                "Tap Refresh Button",
                                modifier = Modifier
                                    .align(Alignment.Center),
                                fontSize = 30.sp,
                                fontFamily = fontFamily
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //Refresh Button
            Button(
                onClick = { moodViewModel.getQuote(context).toString()
                          image.value = getRandomImage(BackgroundImageList)},
                modifier = Modifier
                    .align(
                        Alignment.CenterHorizontally
                    )
                    .size(60.dp),
                colors = ButtonDefaults.buttonColors(Color.Red),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cached,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}


//Get Random Index
private fun getRandomImage(imageArray : Array<String>) : String{

    val randomIndex = Random.nextInt(imageArray.size)
    return imageArray[randomIndex]

}
