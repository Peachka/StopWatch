package com.example.stopwatch.ui.theme

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeModel {
    var counter = 0

    var timeLaps: List<TimeDataModel> = listOf(TimeDataModel(counter, "00:00:00:000000", "00:00:00:000000", "00:00:00:000000"))

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrent(current: String): String{
        counter += 1
        Log.d("TimeModel - counter", counter.toString())
        var newLap = calculateLap(counter, current)

        return formatLap(newLap)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateLap(count:Int, currentTime: String): TimeDataModel{
        Log.d("TimeModel - current time", currentTime)
        if("null" in currentTime){
            return timeLaps[0]
        }
        val formatedTime = formatStr(currentTime)
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss:SSSSSS")
        val timeCurrent = LocalTime.parse(formatedTime, timeFormat)
        val timeBefore = LocalTime.parse(timeLaps[timeLaps.size-1].current, timeFormat)
        val difference = calculateTimeDifference(timeCurrent, timeBefore)

        Log.d("Model","Time difference: $difference")

        var timeDiffStr = ""

        if(difference[0] == 0L){
            timeDiffStr =  String.format("%d:%02d.%s", difference[1], difference[2], difference[3]?.toString()?.take(2) ?: "00")
        }
        else{
            timeDiffStr = String.format("%d:%02d:%02d.%s",difference[0], difference[1], difference[2], difference[3]?.toString()?.take(2) ?: "00")
        }
        var newElement = TimeDataModel(count = count, begin = timeLaps[timeLaps.size-1].current, formatedTime,  timeDiffStr)
        timeLaps  = timeLaps + newElement
        Log.d("TimeModel", newElement.toString())

        return newElement

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateTimeDifference(time1: LocalTime, time2: LocalTime): List<Long> {
        val nanoDifference = time1.toNanoOfDay() - time2.toNanoOfDay()

        val hours = nanoDifference / 3_600_000_000_000
        val minutes = (nanoDifference % 3_600_000_000_000) / 60_000_000_000
        val seconds = (nanoDifference % 60_000_000_000) / 1_000_000_000
        val milliseconds = (nanoDifference % 1_000_000_000) / 1_000_000
        val difference = listOf(hours, minutes, seconds, milliseconds)
        return difference

    }

    fun formatStr(originalTime: String): String{
        var formattedTime = originalTime
        while(formattedTime.length < 15){
            formattedTime += "0"
            Log.d("TimeModel", formattedTime)
        }
        return formattedTime
    }

    fun formatLap(origLap: TimeDataModel): String{
        var count = origLap.count
        var begin = origLap.begin
        var current = origLap.current
        var spent = origLap.timeSpent
        var beginlist = begin.split(":")
        var currentList = current.split(":")


        if (beginlist[0] == "00" && currentList[0] == "00" ){
            var formatedbegin = String.format("%d:%02d.%s", Integer.parseInt(beginlist[1]), Integer.parseInt(beginlist[2]),
                (beginlist[3]).take(2)
            )
            var formatedcurrent = String.format("%d:%02d.%s", Integer.parseInt(currentList[1]), Integer.parseInt(currentList[2]), (currentList[3]).take(2))
            return String.format("№%d %s - %s  %s", count, formatedbegin, formatedcurrent, spent)
        }

        var formatedbegin = String.format("%d:%02d:%02d.%s", Integer.parseInt(beginlist[0]), Integer.parseInt(beginlist[1]), Integer.parseInt(beginlist[2]),(beginlist[3]).take(2))
        var formatedcurrent = String.format("%d:%02d:%02d.%s", Integer.parseInt(currentList[0]), Integer.parseInt(currentList[1]), Integer.parseInt(currentList[2]), (currentList[3]).take(2))



        return String.format("№%d %s - %s  %s", count, formatedbegin, formatedcurrent, spent)

    }

}

