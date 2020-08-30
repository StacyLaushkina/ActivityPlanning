package com.laushkina.activityplanning.ui

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.laushkina.activityplanning.component.common.R
import com.laushkina.activityplanning.ui.navigation.NavigationState

open class BaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info -> { openInfoScreen() }
            R.id.action_plan -> { openPlansScreen() }
            R.id.action_dashboard -> { openDashboardScreen() }
            android.R.id.home -> { finish() }
        }

        return true
    }

    fun initToolbar(title: String, backButtonVisible: Boolean) {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(backButtonVisible)
        supportActionBar?.setDisplayShowHomeEnabled(backButtonVisible)
    }

    fun openPlansScreen() {
        startActivity(Intent(this, NavigationState.instance.navigation!!.getPlansClass()))
    }

    private fun openInfoScreen() {
        startActivity(Intent(this, NavigationState.instance.navigation!!.getInfoClass()))
    }

    private fun openDashboardScreen() {
        startActivity(Intent(this, NavigationState.instance.navigation!!.getDashboardClass()))
    }
}