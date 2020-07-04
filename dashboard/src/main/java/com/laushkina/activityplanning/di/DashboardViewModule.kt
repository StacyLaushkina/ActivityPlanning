package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.ui.dashboard.DashboardView
import dagger.Module
import dagger.Provides

@Module
class DashboardViewModule(private val view: DashboardView) {
    @Provides
    fun dashboardView(): DashboardView {
        return view
    }
}