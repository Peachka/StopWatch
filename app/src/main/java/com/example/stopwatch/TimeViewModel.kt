package com.example.stopwatch

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stopwatch.ui.theme.TimeDataModel
import com.example.stopwatch.ui.theme.TimeModel

class TimeViewModel : ViewModel() {

    private var timeModel = TimeModel()
    private val _timeLaps = MutableLiveData<List<String>>()
    private val timeSec = MutableLiveData<String>()
    private val timeMin = MutableLiveData<String>()
    private val timeMilli = MutableLiveData<String>()
    private val timeHours = MutableLiveData<String>()
    private val nameContinue = MutableLiveData<String>()
    private val nameStop = MutableLiveData<String>()
    private var timer_millisec = 0
    private var timer_sec = 0
    private var timer_min = 0
    private var timer_hour = 0

    private val delay = 10L
    private var isLoopRunning = false

    init{
        loop()
    }

    // Function to pause or reset the loop
    fun butttonStop() {
        isLoopRunning = false
        nameContinue.value = "Continue"
        if(nameStop.value == "Reset"){
            resetLoop()
        }
        nameStop.value = "Reset"
    }

    fun resetLoop(){
        timeSec.value = "00"
        timeMin.value = "00"
        timeMilli.value = "00"
        nameContinue.value = "Start"
        _timeLaps.postValue(emptyList())
        timer_millisec = 0
        timer_min = 0
        timer_sec = 0
        timer_hour = 0
        timeModel = TimeModel()
    }

    // Function to resume the loop
    fun buttonStartAndResume() {
        isLoopRunning = true
        loop()
        nameContinue.value = "Start"
        nameStop.value = "Stop"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun lapButton(){
        val newLap = timeModel.getCurrent("${timeHours.value}:${timeMin.value}:${timeSec.value}:${timer_millisec}")
        val currentList = _timeLaps.value ?: emptyList()
        val updatedList = listOf(newLap) + currentList
        _timeLaps.value = updatedList

        for(i in updatedList){
            Log.d("TimeViewModel", i)
        }
    }

    fun getTime1Data(): LiveData<String> = timeSec
    fun getTime2Data(): LiveData<String> = timeMin
    fun getTime3Data(): LiveData<String> = timeMilli
    fun getButtonNameContinue(): LiveData<String> = nameContinue
    fun getButtonNameStop(): LiveData<String> = nameStop
    fun getTimeLaps(): LiveData<List<String>> = _timeLaps

    private fun loop(){
        if (!isLoopRunning) return
        Handler(Looper.getMainLooper())
            .postDelayed({updateTimeValues()}, delay)

    }

    // Function to update time values
    fun updateTimeValues() {
        timer_millisec += 10

        if(timer_millisec == 1000){
            timer_sec++
            timer_millisec = 0
        }
        timer_sec %= 61
        if (timer_sec == 60){
            timer_sec = 0
            timer_min++

        }

        if(timer_min == 60){
            timer_min = 0
            timer_hour +=1
        }

        timeHours.value = if (timer_hour > 9) "$timer_hour" else "0$timer_hour"
        timeSec.value = if (timer_sec > 9) "$timer_sec" else "0$timer_sec"
        timeMin.value = if (timer_min > 9) "$timer_min" else "0$timer_min"
        timeMilli.value = if (timer_millisec > 100) ".$timer_millisec".subSequence(0,2).toString() else ".0$timer_millisec".subSequence(0,1).toString()

        loop()
    }
}
