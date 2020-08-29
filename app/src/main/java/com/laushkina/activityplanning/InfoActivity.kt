package com.laushkina.activityplanning

import android.os.Bundle
import com.laushkina.activityplanning.ui.BaseActivity

class InfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_activity)
        initToolbar("Info", true)
    }
}
