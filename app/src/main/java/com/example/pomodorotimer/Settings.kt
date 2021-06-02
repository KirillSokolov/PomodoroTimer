package com.example.pomodorotimer

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
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

        var pomodoroValue: NumberPicker = binding.pomodoroTime
        pomodoroValue.setMinValue(1);
        pomodoroValue.setMaxValue(60);
        pomodoroValue.setWrapSelectorWheel(true);
        pomodoroValue.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        var shortValue: NumberPicker = binding.shortTime
        shortValue.setMinValue(1);
        shortValue.setMaxValue(60);
        shortValue.setWrapSelectorWheel(true);
        shortValue.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        var longValue: NumberPicker = binding.longTime
        longValue.setMinValue(1);
        longValue.setMaxValue(60);
        longValue.setWrapSelectorWheel(true);
        longValue.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply {
            val pomodoro = getInt("1", 25)
            val shortTime = getInt("2", 5)
            val longTime = getInt("3", 10)

            binding.pomodoroTime.value = pomodoro
            binding.shortTime.value = shortTime
            binding.longTime.value = longTime

            var checked = getBoolean("CHECKED", false)
            binding.darkTheme.isChecked = checked

        }
    }

    fun saveData(v: View?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        var pomodoroTimeGet = binding.pomodoroTime.value
        var shortTimeGet = binding.shortTime.value
        var longTimeGet = binding.longTime.value

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