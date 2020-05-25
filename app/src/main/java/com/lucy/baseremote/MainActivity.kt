package com.lucy.baseremote

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                switchToDashboard()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_power -> {
                switchToPower()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchToDashboard()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    private fun switchToDashboard() {
        val dashFragment = DashboardFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, dashFragment)
        ft.commit()
    }

    private fun switchToPower() {
        val powerFragment = PowerFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, powerFragment)
        ft.commit()
    }


}
