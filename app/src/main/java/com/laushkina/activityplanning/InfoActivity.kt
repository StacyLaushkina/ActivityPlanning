package com.laushkina.activityplanning

import android.os.Bundle
import com.laushkina.activityplanning.ui.BaseActivity
import kotlinx.android.synthetic.main.info_activity.*

class InfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_activity)
        initToolbar("Info", true)

        plan_button.setOnClickListener { openPlansScreen() }
        track_button.setOnClickListener { openTrackScreen() }
        dashboard_button.setOnClickListener { openDashboardScreen() }
    }
}
