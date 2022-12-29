package com.example.astonintensive4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.astonintensive4.coreui.views.ClockView
import com.example.astonintensive4.utils.find

class MainActivity : AppCompatActivity() {

    private val clock by find<ClockView>(R.id.clock)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Для проверки
         */
        /*
        val mockClockViewModel = ClockViewModel(
            hourHandColor = resources.getColor(R.color.purple_700, this.theme),
            minuteHandColor = resources.getColor(R.color.teal_700, this.theme),
            secondHandColor = resources.getColor(R.color.black, this.theme),
            secondHandLength = 300,
            minuteHandLength = 200,
            hourHandLength = 100
        )
        clock.populate(mockClockViewModel)
        */


        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                clock.updateClock()
                mainHandler.postDelayed(this, COUNTER_TICK_MILS)
            }
        })
    }

    companion object {
        private const val COUNTER_TICK_MILS = 1000L
    }
}