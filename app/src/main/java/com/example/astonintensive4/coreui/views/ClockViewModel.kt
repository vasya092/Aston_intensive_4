package com.example.astonintensive4.coreui.views

import com.example.astonintensive4.coreui.BaseItem

data class ClockViewModel(
    val hourHandColor: Int,
    val minuteHandColor: Int,
    val secondHandColor: Int,
    val secondHandLength: Int = 0,
    val minuteHandLength: Int = 0,
    val hourHandLength: Int = 0
): BaseItem