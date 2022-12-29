package com.example.astonintensive4.utils

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun getSecondAngle(seconds: Int): Float = 6f * seconds
fun getMinuteAngle(seconds: Int, minutes: Int): Float = 6f * (minutes + (1f / 60f) * seconds)
fun getHourAngle(hour: Int, minutes: Int): Float = 30f * (hour + (1f / 60f) * minutes)

fun getXForClockNumber(num: Int, xCenter: Float, radius: Float): Float {
    return xCenter + (radius - 30f) * cos(-30f * num * (PI / 180f) + PI / 2f).toFloat()
}

fun getYForClockNumber(num: Int, yCenter: Float, radius: Float): Float {
    return yCenter - (radius - 30f) * sin(-30f * num * (PI / 180f) + PI / 2f).toFloat()
}