package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.ui.DashboardPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DashboardPresenterModule::class])
interface DashboardPresenterComponent {
    fun getDashboardPresenter(): DashboardPresenter
}