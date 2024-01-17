package com.example.stopwatch

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatch.ui.theme.StopWatchTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn


class MainActivity : ComponentActivity() {
    private val timeViewModel by viewModels<TimeViewModel>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adaptiveParametrs = AdaptiveParametrs(this)

        val density = resources.displayMetrics.density
        val screenWidthDp = adaptiveParametrs.getMyScreenWidth() / density
        val screenHeightDp = adaptiveParametrs.getMyScreenHeight() / density
        Log.d("MainActivity", "ScreenW = $screenWidthDp")
        Log.d("MainActivity", "ScreenH = $screenHeightDp")

        setContent {
            StopWatchTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray) // Set the background color here
                ) {
                    TimeText(timeViewModel, screenHeightDp, screenWidthDp)
                }

            }
        }
    }
}

@Composable
fun AppContent() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        color = MaterialTheme.colorScheme.background
    ) {

    }
}
@Composable
fun lazy(timeViewModel: TimeViewModel, screenH: Float){
    val itemList by timeViewModel.getTimeLaps().observeAsState(initial = emptyList())

    LazyColumn (modifier = Modifier
        .fillMaxWidth()
        .height((screenH*0.11).dp)){
        items(itemList.size) { message ->
            Text( text = itemList[message],
                color = Color.White,
                fontSize = 20.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeText(timeViewModel: TimeViewModel, screenHeight: Float, screenWidth: Float){
    val timeSec by timeViewModel.getTime1Data().observeAsState("00")
    val timeMin by timeViewModel.getTime2Data().observeAsState("00")
    val timeMilli by timeViewModel.getTime3Data().observeAsState("00")
    val nameStart by timeViewModel.getButtonNameContinue().observeAsState("Start")
    val nameStop by timeViewModel.getButtonNameStop().observeAsState("Stop")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .weight(1f)
                .padding(vertical = 0.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ){
            lazy(timeViewModel, screenHeight)

        }
        // Row positioned at the bottom
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .weight(0.09f),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {

            // Your Row elements
            ButtonLayout(onClick =  {timeViewModel.buttonStartAndResume()}, nameStart, Modifier
                .padding(2.dp)
                .weight(1f) )

            ButtonLayout(onClick =  {timeViewModel.butttonStop()},nameStop, Modifier
                .padding(2.dp)
                .weight(1f) )

            ButtonLayout(onClick =  {timeViewModel.lapButton()},"Lap", Modifier
                .padding(2.dp)
                .weight(1f) )

        }
    }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight * 3 / 4).toInt().dp)
                .wrapContentSize(Alignment.Center)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),

                contentAlignment = Alignment.TopCenter
            ) {
                // Milliseconds
                Text(
                    text = timeMilli,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    // Set the desired text size
                    modifier = Modifier
                        .padding(8.dp)
                        .height(50.dp)
                        .offset(y = 470.dp, x = 150.dp),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                    fontSize = 40.sp// Adjust the y value to position the text at the top
                )


                Text(
                    text = timeSec,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    // Set the desired text size
                    modifier = Modifier
                        .padding(8.dp)
                        .offset(y = (screenHeight * 0.22).dp)
                        ,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                    fontSize = (screenWidth*0.78f).sp// Adjust the y value to position the text at the top
                )

                Text(
                    text = timeMin,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .offset(y = (-screenHeight * 0.12).dp),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                    fontSize = (screenWidth * 0.78f).sp
                )
            }
        }
    }

@Composable
fun ButtonLayout(onClick: () -> Unit, name: String, modifier: Modifier){
    Button(
        onClick =  onClick ,
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(2.dp, Color.White),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White, containerColor = Color.Black)

        ) {
        Text(
            text = name,
            fontSize = 19.sp,
            modifier = Modifier.padding((0).dp))
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(widthDp = 393, heightDp = 719)
fun TimeTextPreview() {

//    lazy()
    val timeViewModel = TimeViewModel()
    TimeText(timeViewModel,718.9091f,  392.72726f )
}
