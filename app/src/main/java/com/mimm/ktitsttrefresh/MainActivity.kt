package com.mimm.ktitsttrefresh

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mimm.ktitsttrefresh.Logic.LearnDay
import com.mimm.ktitsttrefresh.Logic.Lesson
import com.mimm.ktitsttrefresh.databinding.ActivityMainBinding
import com.mimm.ktitsttrefresh.ui.PeriodViewer
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalTime

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    var today: LearnDay? = null
    var tomorrow: LearnDay? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTimes()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun loadTimes() {
        var gs = Gson()
        var day = LocalDate.now().dayOfWeek.value
        val less = object : TypeToken<Array<Array<Lesson?>>>() {}.type
        var allDaysText = resources.openRawResource(R.raw.g122).readBytes()
            .toString(Charset.defaultCharset())
        var allDays: Array<Array<Lesson?>> = gs.fromJson(
            allDaysText, less
        )
        if (day in 1..4) {
            var tt = LoadCalls(R.raw.lesson_call_main, gs)
            var nowDay: Array<Lesson?> = allDays[day - 1]
            var nextDay: Array<Lesson?> = allDays[day]
            today = LearnDay(tt, nowDay)
            tomorrow = LearnDay(tt, nextDay)
        } else if (day == 6) {
            val less = object : TypeToken<Array<Array<Lesson?>>>() {}.type
            var nextDay: Array<Lesson?> = allDays[0]
            var nowDay: Array<Lesson?> = allDays[day - 1]
            today = LearnDay(LoadCalls(R.raw.lesson_call_sat, gs), nowDay)
            tomorrow = LearnDay(LoadCalls(R.raw.lesson_call_main, gs), nextDay)
        } else if (day == 7) {
            tomorrow = LearnDay(LoadCalls(R.raw.lesson_call_main, gs), allDays[0])
        }
        if (day == 5) {
            tomorrow!!.CallsTimetable = LoadCalls(R.raw.lesson_call_sat, gs)
        }
    }

    fun LoadCalls(id: Int, gs: Gson): Array<LocalTime> {
        val listType: Type = object : TypeToken<List<Long>?>() {}.type
        var ttLong: List<Long> = gs.fromJson(
            resources.openRawResource(id).readBytes()
                .toString(Charset.defaultCharset()), listType
        )
        var tt = Array(11) { LocalTime.MIN }
        for (i in 0 until 11) {
            tt[i] = LocalTime.ofSecondOfDay(ttLong[i])
        }
        return tt
    }
}