package com.laushkina.activityplanning.di.dashboard

import com.laushkina.activityplanning.ui.dashboard.DashboardPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DashboardPresenterModule::class])
interface DashboardPresenterComponent {
    fun getDashboardPresenter(): DashboardPresenter
}