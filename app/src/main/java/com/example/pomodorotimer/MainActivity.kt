package com.example.pomodorotimer

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pomodorotimer.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun test(view: View) {
        var mTextField = binding.standardTime
        val simpleDateFormat = SimpleDateFormat("mm:ss")

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var date = millisUntilFinished
                var dateText = simpleDateFormat.format(date)
                mTextField.setText(dateText)
            }

            override fun onFinish() {

            }

        }.start()


    }
}