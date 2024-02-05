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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.constraintlayout.compose.ConstraintLayout
import kotlin.concurrent.timer


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
        .height((screenH * 0.11).dp)){
        items(itemList.size) { message ->
            Text( text = itemList[message],
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    Log.d("TextField", itemList[message])
                })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeText(timeViewModel: TimeViewModel, screenHeight: Float, screenWidth: Float){
    val timeSec by timeViewModel.getTime1Data().observeAsState("00")
    val timeMin by timeViewModel.getTime2Data().observeAsState("00")
    val timeMilli by timeViewModel.getTime3Data().observeAsState("00")
    val timeHours by timeViewModel.getTime4Data().observeAsState("00")
    val nameStart by timeViewModel.getButtonNameContinue().observeAsState("Start")
    val nameStop by timeViewModel.getButtonNameStop().observeAsState("Stop")

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        content = {
            val (upperRow, bottomRow, timerDisplay) = createRefs()

            // Upper Row
            Row(
                modifier = Modifier
                    .constrainAs(upperRow) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(timerDisplay.bottom)
                        bottom.linkTo(bottomRow.top)
                    }
                    .background(Color.Black)
                    .padding(horizontal = 10.dp)
            ) {
                lazy(timeViewModel = timeViewModel, screenH = screenHeight)
            }

            // Bottom Row
            Row(
                modifier = Modifier
                    .constrainAs(bottomRow) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .background(Color.Black)
                    ,
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonLayout(
                    onClick = { timeViewModel.buttonStartAndResume() },
                    name = nameStart,
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1f)
                )

                ButtonLayout(
                    onClick = { timeViewModel.butttonStop() },
                    name = nameStop,
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1f)
                )

                ButtonLayout(
                    onClick = { timeViewModel.lapButton() },
                    name = "Lap",
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(1f)
                )
            }

            // Timer Display
            Column(
                modifier = Modifier
                    .constrainAs(timerDisplay) {
                        top.linkTo(parent.top)
                        bottom.linkTo(upperRow.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .height((screenHeight * 3 / 4).toInt().dp)
                    .wrapContentSize(Alignment.Center)
                    ,
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {


                    if (Integer.parseInt(timeHours) != 0){

                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
                            content = {
                                val (uppertext, centerText, centerText2, bottomtext) = createRefs()

                                testText(text = timeMilli, modifier = Modifier
                                    .padding(0.dp)
                                    .constrainAs(bottomtext) {
                                        bottom.linkTo(upperRow.top)
                                        end.linkTo(parent.end, margin = 5.dp)
                                        top.linkTo(centerText.bottom)
                                    }
                                    .offset(y = (-195).dp)
                                    )


                                testText(text = timeSec, modifier = Modifier
                                    .padding(0.dp)
                                    .constrainAs(centerText) {
                                        end.linkTo(parent.end)
                                        start.linkTo(parent.start)
                                        top.linkTo(uppertext.bottom)
                                        bottom.linkTo(bottomtext.top)
                                    }
                                    .offset(y = (-155).dp),
                                    fontS = screenWidth * 0.62f )

                                testText(text = timeMin, modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(uppertext) {
                                        top.linkTo(centerText2.bottom)
                                        bottom.linkTo(centerText.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .offset(y = (-102).dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally) // Ensure content wraps horizontally
                                    .wrapContentHeight(Alignment.CenterVertically)
                                    , screenWidth * 0.62f)

                                testText(text = timeHours, modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(centerText2) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(uppertext.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .offset(y = (-50).dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally) // Ensure content wraps horizontally
                                    .wrapContentHeight(Alignment.CenterVertically)
                                    , screenWidth * 0.62f)
                            })



                    } else{

                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
                            content = {
                                val (uppertext, centerText, bottomtext) = createRefs()


                                testText(text = timeMilli, modifier = Modifier
                                    .padding(0.dp)
                                    .constrainAs(bottomtext) {
                                        bottom.linkTo(upperRow.top)
                                        end.linkTo(parent.end, margin = 5.dp)
                                        top.linkTo(centerText.bottom)
                                    }
                                    .offset(y = (-105).dp))

                                testText(text = timeSec, modifier = Modifier
                                    .padding(0.dp)
                                    .constrainAs(centerText) {
                                        end.linkTo(parent.end)
                                        start.linkTo(parent.start)
                                        top.linkTo(uppertext.bottom)
                                        bottom.linkTo(bottomtext.top)
                                    }
                                    .offset(y = (-105).dp),
                                    fontS = screenWidth * 0.78f )

                                testText(text = timeMin, modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(uppertext) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(centerText.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .offset(y = (-50).dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally) // Ensure content wraps horizontally
                                    .wrapContentHeight(Alignment.CenterVertically)
                                    , screenWidth * 0.78f)

                            }
                        )
                    }
                    }
                }
            })
        }


@Composable
fun testText(text: String, modifier: Modifier, fontS: Float = 50f) {
    Text(
        text = text,
        color = Color.LightGray,
        fontWeight = FontWeight.Bold,
        modifier = modifier.height(if (fontS == 50f) fontS.dp else (fontS - 10).dp),
        textAlign = TextAlign.Center,
        fontSize = fontS.sp,
         // Set padding to zero
        style = LocalTextStyle.current.merge(
            TextStyle(
                lineHeight = (fontS-30).sp, // Set lineHeight to match fontSize or desired value
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Top,
                    trim = LineHeightStyle.Trim.FirstLineTop
                )
            )
        )
    )
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

//    testText("00", modifier = Modifier.padding(0.dp), 393.2f)

//    lazy()
    val timeViewModel = TimeViewModel()
    TimeText(timeViewModel,718.9091f,  392.72726f )
}
