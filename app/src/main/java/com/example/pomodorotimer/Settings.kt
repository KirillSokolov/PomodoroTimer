package com.example.pomodorotimer

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.pomodorotimer.databinding.ActivityMainBinding
import com.example.pomodorotimer.databinding.ActivitySettingsBinding
import kotlin.math.absoluteValue

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply {
            val pomodoro = getInt("1", 25)
            val shortTime = getInt("2", 5)
            val longTime = getInt("3", 10)

            binding.pomodoroTime.setText(pomodoro.toString())
            binding.shortTime.setText(shortTime.toString())
            binding.longTime.setText(longTime.toString())

            var checked = getBoolean("CHECKED", false)
            binding.darkTheme.isChecked = checked

        }
    }

    fun saveData(v: View?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        var pomodoroTimeGet = binding.pomodoroTime.text.toString().toInt()
        var shortTimeGet = binding.shortTime.text.toString().toInt()
        var longTimeGet = binding.longTime.text.toString().toInt()

        editor
            .putInt("1", pomodoroTimeGet)
            .putInt("2", shortTimeGet)
            .putInt("3", longTimeGet)
            .apply()

        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
    }

    fun returnMain(v: View?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    fun darkTheme(v: View?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        var checked = binding.darkTheme.isChecked
        editor.putBoolean("CHECKED", checked).apply()

        if (checked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}