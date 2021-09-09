package com.example.pomodorotimer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.pomodorotimer.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.imageButton.setOnClickListener {
            returnMain()
        }
        binding.apply.setOnClickListener {
            saveData()
        }
        binding.darkTheme.setOnClickListener {
            darkTheme()
        }
        binding.notificationEnabled.setOnClickListener {
            setNotification()
        }

        val pomodoroValue: NumberPicker = binding.pomodoroTime
        pomodoroValue.minValue = 1
        pomodoroValue.maxValue = 60
        pomodoroValue.wrapSelectorWheel = true
        pomodoroValue.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        val shortValue: NumberPicker = binding.shortTime
        shortValue.minValue = 1
        shortValue.maxValue = 60
        shortValue.wrapSelectorWheel = true
        shortValue.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        val longValue: NumberPicker = binding.longTime
        longValue.minValue = 1
        longValue.maxValue = 60
        longValue.wrapSelectorWheel = true
        longValue.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        val pref = getSharedPreferences(
            "com.example.pomodorotimer", Context.MODE_PRIVATE
        )
        pref.apply {
            val pomodoro = getInt("1", 25)
            val shortTime = getInt("2", 5)
            val longTime = getInt("3", 10)
            val notificationE = getBoolean("NOTIFICATION", true)

            binding.pomodoroTime.value = pomodoro
            binding.shortTime.value = shortTime
            binding.longTime.value = longTime
            binding.notificationEnabled.isChecked = notificationE

            val checked = getBoolean("CHECKED", false)
            binding.darkTheme.isChecked = checked

        }
    }

    private fun setNotification() {
        val pref = getSharedPreferences(
            "com.example.pomodorotimer", Context.MODE_PRIVATE
        )
        val editor = pref.edit()
        val statusNotification = binding.notificationEnabled.isChecked

        if (statusNotification) {
            editor.putBoolean("NOTIFICATION", statusNotification).apply()
        } else {
            editor.putBoolean("NOTIFICATION", statusNotification).apply()
        }
    }

    private fun saveData() {
        val pref = getSharedPreferences(
            "com.example.pomodorotimer", Context.MODE_PRIVATE
        )
        val editor = pref.edit()

        val pomodoroTimeGet = binding.pomodoroTime.value
        val shortTimeGet = binding.shortTime.value
        val longTimeGet = binding.longTime.value

        editor
            .putInt("1", pomodoroTimeGet)
            .putInt("2", shortTimeGet)
            .putInt("3", longTimeGet)
            .apply()

        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
    }

    private fun returnMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun darkTheme() {
        val pref = getSharedPreferences(
            "com.example.pomodorotimer", Context.MODE_PRIVATE
        )
        val editor = pref.edit()

        val checked = binding.darkTheme.isChecked
        editor.putBoolean("CHECKED", checked).apply()

        if (checked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}