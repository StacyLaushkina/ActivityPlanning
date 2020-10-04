package com.laushkina.activityplanning

import android.app.Application
import com.laushkina.activityplanning.ui.navigation.NavigationState

class PlanningApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NavigationState.instance.navigation = NavigationImpl()
    }
}