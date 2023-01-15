package com.mimm.ktitsttrefresh

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mimm.ktitsttrefresh.databinding.ActivityMainBinding
import com.mimm.ktitsttrefresh.ui.PeriodViewer
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    public fun onAdd(view: View){
        var m : LinearLayout = findViewById(R.id.MainLL)

        var newPV = PeriodViewer(this)
        var ltOpt = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        newPV.layoutParams = ltOpt
        newPV.setPadding(0, 10, 0, 10)
        newPV.eventEnd = LocalTime.of(22, 30)
        newPV.eventStart = LocalTime.of(21, 0)
        m.addView(newPV)

    }

    public fun onAct(view: View){
        var m : LinearLayout = findViewById(R.id.MainLL)
        var lastPV = (m[m.childCount - 1] as PeriodViewer)
        lastPV.activate()
        lastPV.updateProgress()
    }
}