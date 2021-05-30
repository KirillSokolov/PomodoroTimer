package com.example.pomodorotimer

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pomodorotimer.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var continueTimer = true
    var miliTimer: Long = 0
    var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun test(view: View) {
        var mTextField = binding.standardTime
        val simpleDateFormat = SimpleDateFormat("mm:ss")

        var timer: Long = 60 * 25 * 1000

        if (isInit) {
            timer = miliTimer
        }

        Toast.makeText(applicationContext, "Starting one Pomodoro", Toast.LENGTH_SHORT).show()

        continueTimer = true

        val count = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                isInit = true
                miliTimer = millisUntilFinished
                var date = millisUntilFinished
                var dateText = simpleDateFormat.format(date)
                mTextField.setText(dateText)
                if (!continueTimer) {
                    cancel()
                }
            }
            override fun onFinish() {
                mTextField.setText("00:00")
            }
        }.start()
    }

    fun cancelTimer(view: View) {
        continueTimer = false
    }

    fun resetTimer(view: View) {
        cancelTimer(view)
        isInit = false
        binding.standardTime.setText("25:00")
    }
}