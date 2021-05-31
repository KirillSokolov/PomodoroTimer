package com.example.pomodorotimer

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.pomodorotimer.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var continueTimer = true
    var miliTimer: Long = 0
    var isInit = false
    var timer: Long = 60 * 25 * 1000
    var check = 1
    var reset = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.pomodoro.setBackgroundColor(resources.getColor(R.color.holo_green_dark))

    }

    fun notification(title: String?, message: String?, context: Context) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = createID()
        val channelId = "channel-id"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.sym_def_app_icon) //R.mipmap.ic_launcher
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(longArrayOf(100, 250))
            .setLights(Color.YELLOW, 500, 5000)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.holo_green_dark))
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(Intent(context, MainActivity::class.java))
        val resultPendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(resultPendingIntent)
        notificationManager.notify(notificationId, mBuilder.build())
    }

    fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(now).toInt()
    }

    fun test(view: View) {
        var mTextField = binding.standardTime
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        reset = false

        if (isInit) {
            timer = miliTimer
        }

        continueTimer = true

        if (!isInit) {
            isInit = true
        }

        val count = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                miliTimer = millisUntilFinished
                var date = millisUntilFinished
                var dateText = simpleDateFormat.format(date)
                mTextField.setText(dateText)
                if (!continueTimer) {
                    cancel()
                    if (reset) {
                        when (check) {
                            1 -> binding.standardTime.setText("25:00")
                            2 -> binding.standardTime.setText("5:00")
                            3 -> binding.standardTime.setText("10:00")
                        }
                    }
                }
            }

            override fun onFinish() {
                mTextField.setText("00:00")
                notification("PomodoroTimer", "The time is over", applicationContext)
            }
        }.start()
    }

    fun cancelTimer(view: View) {
        continueTimer = false
    }

    fun resetTimer(view: View) {
        reset = true
        cancelTimer(view)
        isInitFalse()

        if (reset) {
            when (check) {
                1 -> binding.standardTime.setText("25:00")
                2 -> binding.standardTime.setText("5:00")
                3 -> binding.standardTime.setText("10:00")
            }
        }
    }

    fun pomodoroTimer(view: View) {
        resetTimer(view)
        isInitFalse()
        timer = 60 * 25 * 1000
        binding.standardTime.setText("25:00")
        check = 1
        resetColor()
        binding.pomodoro.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
    }

    fun shortBreak(view: View) {
        resetTimer(view)
        isInitFalse()
        timer = 60 * 5 * 1000
        binding.standardTime.setText("05:00")
        check = 2
        resetColor()
        binding.shortBreak.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
    }

    fun longBreak(view: View) {
        resetTimer(view)
        isInitFalse()
        timer = 60 * 10 * 1000
        binding.standardTime.setText("10:00")
        check = 3
        resetColor()
        binding.longBreak.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
    }

    fun isInitFalse() {
        if (isInit) {
            isInit = false
        }
    }

    fun resetColor() {
        binding.pomodoro.setBackgroundColor(resources.getColor(R.color.darker_gray))
        binding.shortBreak.setBackgroundColor(resources.getColor(R.color.darker_gray))
        binding.longBreak.setBackgroundColor(resources.getColor(R.color.darker_gray))
    }

}