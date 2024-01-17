package com.example.stopwatch
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

class AdaptiveParametrs constructor(context: Context){

    val screenSize = getScreenSize(context)
    val screenWidth = screenSize.first
    val screenHeight = screenSize.second

    fun getMyScreenWidth(): Int{
        return screenWidth
    }

    fun getMyScreenHeight(): Int{
        return screenHeight
    }

    fun getScreenSize(context: Context): Pair<Int, Int> {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        return Pair(screenWidth, screenHeight)
    }
}