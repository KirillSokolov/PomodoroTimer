package com.example.pomodorotimer

import android.R
import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    var isInit = false
    var timer: Long = 60 * 25 * 1000
    var check = 1
    var reset = false
    var pomodoroTimerGlobal = 25
    var shortTimeGlobal = 5
    var longTimeGlobal = 10
    val simpleDateFormat = SimpleDateFormat("mm:ss")
    var checkedThemeGlobal = false
    var date = miliTimer
    var notificationId = createID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            binding.pomodoro.setBackgroundColor(resources.getColor(R.color.holo_green_dark))

            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.apply {
                val pomodoro = getInt("1", 25)
                val shortTime = getInt("2", 5)
                val longTime = getInt("3", 10)
                val checked = getBoolean("CHECKED", false)

                pomodoroTimerGlobal = pomodoro
                shortTimeGlobal = shortTime
                longTimeGlobal = longTime
                checkedThemeGlobal = checked
        }

        if (checkedThemeGlobal) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        pomodoroTimer(view)
        resetTimer(view)

    }

    fun notification(title: String?, message: String?, context: Context) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = createID()
        val channelId = "channel-id"
        val channelName = "Channel Name"

        var uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

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
            .setSound(uri)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun test(view: View) {
        var mTextField = binding.standardTime
        reset = false

        if (isInit) {
            timer = miliTimer
        }

        continueTimer = true

        if (!isInit) {
            isInit = true
        }

        var mBuilder: NotificationCompat.Builder = onTopNotification("Pomodoro", applicationContext)

        val count = object : CountDownTimer(timer, 1000) {
            @RequiresApi(Build.VERSION_CODES.O)
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
                    var date = millisUntilFinished
                    var dateText = simpleDateFormat.format(date)
                    mTextField.setText(dateText)
                }

                var date = millisUntilFinished
                var dateText = simpleDateFormat.format(date)

                onTopNotificationTick(dateText, mBuilder)
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
                1 -> binding.standardTime.text = simpleDateFormat.format(pomodoroTimerGlobal * 60 * 1000)
                2 -> binding.standardTime.text = simpleDateFormat.format(shortTimeGlobal * 60 * 1000)
                3 -> binding.standardTime.text = simpleDateFormat.format(longTimeGlobal * 60 * 1000)
            }
        }
    }

    fun pomodoroTimer(view: View) {
        check = 1
        resetTimer(view)
        isInitFalse()
        timer = 60 * pomodoroTimerGlobal * 1000L
        binding.standardTime.text = simpleDateFormat.format(pomodoroTimerGlobal * 60 * 1000)

        resetColor()
        binding.pomodoro.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
        binding.pomodoro.setTextColor(resources.getColor(R.color.white))
    }

    fun shortBreak(view: View) {
        check = 2
        resetTimer(view)
        isInitFalse()
        timer = 60 * shortTimeGlobal * 1000L
        binding.standardTime.text = simpleDateFormat.format(shortTimeGlobal * 60 * 1000)

        resetColor()
        binding.shortBreak.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
        binding.shortBreak.setTextColor(resources.getColor(R.color.white))
    }

    fun longBreak(view: View) {
        check = 3
        resetTimer(view)
        isInitFalse()
        timer = 60 * longTimeGlobal * 1000L
        binding.standardTime.text = simpleDateFormat.format(longTimeGlobal * 60 * 1000)
        resetColor()
        binding.longBreak.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
        binding.longBreak.setTextColor(resources.getColor(R.color.white))
    }

    fun isInitFalse() {
        if (isInit) {
            isInit = false
        }
    }

    fun resetColor() {
        binding.pomodoro.setBackgroundColor(resources.getColor(R.color.darker_gray))
        binding.pomodoro.setTextColor(resources.getColor(R.color.black))
        binding.shortBreak.setBackgroundColor(resources.getColor(R.color.darker_gray))
        binding.shortBreak.setTextColor(resources.getColor(R.color.black))
        binding.longBreak.setBackgroundColor(resources.getColor(R.color.darker_gray))
        binding.longBreak.setTextColor(resources.getColor(R.color.black))
    }

    fun settings(v: View) {
        cancelTimer(v)
        val intent = Intent(applicationContext, Settings::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTopNotification(title: String?,
                          context: Context) : NotificationCompat.Builder {

        var mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH

        var existChannel : NotificationChannel? = mNotificationManager.getNotificationChannel("Pomodoro timer")
        var chamaExiste = existChannel?.id.equals("Pomodoro timer") ?: false

        val channelId = "Pomodoro Timer"
        val channelName = "Countdown"

        if (chamaExiste) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    channelId, channelName, importance
                )
                mNotificationManager.createNotificationChannel(mChannel)
            }
        }

        var dateText = simpleDateFormat.format(date)

        var numMessages = 0
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentText(dateText)
            .setContentTitle(title)
            .setNumber(++numMessages)
            .setSmallIcon(R.mipmap.sym_def_app_icon) //R.mipmap.ic_launcher
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.holo_green_dark))
            .setOnlyAlertOnce(true)

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(Intent(context, MainActivity::class.java))

        mNotificationManager.notify(
            notificationId,
            mBuilder.build()
        )

        return mBuilder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTopNotificationTick(value: String, mBuilder: NotificationCompat.Builder) {

        var mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "Pomodoro Timer"

//        var dateText = simpleDateFormat.format(date)

        mBuilder.setContentText(value);
        mNotificationManager.notify(
            notificationId,
            mBuilder.build())

    }

}