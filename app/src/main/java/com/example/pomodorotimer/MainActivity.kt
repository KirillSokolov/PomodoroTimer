package com.example.pomodorotimer

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.pomodorotimer.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var continueTimer = true
    var miliTimer: Long = 0
    private var isInit = false
    var timer: Long = 60 * 25 * 1000
    var check = 1
    var reset = false
    var pomodoroTimerGlobal = 25
    var shortTimeGlobal = 5
    var longTimeGlobal = 10
    val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    var checkedThemeGlobal = false
    var notificationEnabled = true

    // Notification
    val CHANNEL_ID = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            binding.pomodoro.setBackgroundColor(ContextCompat.getColor(
                applicationContext,
                R.color.holo_green_dark
            ))

            val pref = getSharedPreferences(
            "com.example.pomodorotimer", Context.MODE_PRIVATE)
            pref.apply {
                val pomodoro = getInt("1", 25)
                val shortTime = getInt("2", 5)
                val longTime = getInt("3", 10)
                val checked = getBoolean("CHECKED", false)
                val notificationE = getBoolean("NOTIFICATION", true)

                pomodoroTimerGlobal = pomodoro
                shortTimeGlobal = shortTime
                longTimeGlobal = longTime
                checkedThemeGlobal = checked
                notificationEnabled = notificationE
        }

        if (checkedThemeGlobal) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        pomodoroTimer()
        resetTimer()

        binding.pomodoro.setOnClickListener {
            pomodoroTimer()
        }
        binding.shortBreak.setOnClickListener {
            shortBreak()
        }
        binding.longBreak.setOnClickListener {
            longBreak()
        }
        binding.start.setOnClickListener {
            start()
        }
        binding.stop.setOnClickListener {
            cancelTimer()
        }
        binding.reset.setOnClickListener {
            resetTimer()
        }
        binding.settingsButton.setOnClickListener {
            settings()
        }

    }


    private fun start() {
        val mTextField = binding.standardTime
        reset = false

        if (isInit) {
            timer = miliTimer
        }

        continueTimer = true

        if (!isInit) {
            isInit = true
        }

        object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (!continueTimer) {
                    cancel()
                    if (reset) {
                        when (check) {
                            1 -> binding.standardTime.text = simpleDateFormat.format(pomodoroTimerGlobal * 60 * 1000)
                            2 -> binding.standardTime.text = simpleDateFormat.format(shortTimeGlobal * 60 * 1000)
                            3 -> binding.standardTime.text = simpleDateFormat.format(longTimeGlobal * 60 * 1000)
                        }
                    }
                } else {
                    miliTimer = millisUntilFinished
                        val dateText = simpleDateFormat.format(millisUntilFinished)
                    mTextField.text = dateText
                }

            }

            override fun onFinish() {
                mTextField.text = "00:00"

                if (notificationEnabled) {
                    val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification_clear_all)
                        .setContentTitle("Pomodoro Timer")
                        .setContentText("Timer is finished")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val name = "Notification timer"
                        val descriptionText = "Channel to notify timer."
                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                            description = descriptionText
                        }
                        // Register the channel with the system
                        val notificationManager: NotificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(channel)

                        notificationManager.notify(1, builder.build())
                    } else {
                        val notificationManager: NotificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.notify(1, builder.build())
                    }
                }

                resetTimer()
            }
        }.start()
    }

    private fun cancelTimer() {
        continueTimer = false
    }

    private fun resetTimer() {
        reset = true
        cancelTimer()
        isInitFalse()

        if (reset) {
            when (check) {
                1 -> binding.standardTime.text = simpleDateFormat.format(pomodoroTimerGlobal * 60 * 1000)
                2 -> binding.standardTime.text = simpleDateFormat.format(shortTimeGlobal * 60 * 1000)
                3 -> binding.standardTime.text = simpleDateFormat.format(longTimeGlobal * 60 * 1000)
            }
        }
    }

    fun pomodoroTimer() {
        check = 1
        resetTimer()
        isInitFalse()
        timer = 60 * pomodoroTimerGlobal * 1000L
        binding.standardTime.text = simpleDateFormat.format(pomodoroTimerGlobal * 60 * 1000)

        resetColor()
        binding.pomodoro.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.holo_green_dark))
        binding.pomodoro.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
    }

    private fun shortBreak() {
        check = 2
        resetTimer()
        isInitFalse()
        timer = 60 * shortTimeGlobal * 1000L
        binding.standardTime.text = simpleDateFormat.format(shortTimeGlobal * 60 * 1000)

        resetColor()
        binding.shortBreak.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.holo_green_dark))
        binding.shortBreak.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
    }

    private fun longBreak() {
        check = 3
        resetTimer()
        isInitFalse()
        timer = 60 * longTimeGlobal * 1000L
        binding.standardTime.text = simpleDateFormat.format(longTimeGlobal * 60 * 1000)
        resetColor()
        binding.longBreak.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.holo_green_dark))
        binding.longBreak.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
    }

    private fun isInitFalse() {
        if (isInit) {
            isInit = false
        }
    }

    private fun resetColor() {
        binding.pomodoro.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.darker_gray))
        binding.pomodoro.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        binding.shortBreak.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.darker_gray))
        binding.shortBreak.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        binding.longBreak.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.darker_gray))
        binding.longBreak.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
    }

    private fun settings() {
        cancelTimer()
        val intent = Intent(applicationContext, Settings::class.java)
        startActivity(intent)
    }


}