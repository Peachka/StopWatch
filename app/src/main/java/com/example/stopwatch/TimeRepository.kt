package com.example.stopwatch

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class TimeRepository {
    private val _time1 = mutableStateOf("10")
    val time1: State<String> get() = _time1

    private val _time2 = mutableStateOf("00")
    val time2: State<String> get() = _time2

    // Function to update the time values
    fun updateTimeValues(newTime1: String, newTime2: String) {
        _time1.value = newTime1
        _time2.value = newTime2
    }
}
