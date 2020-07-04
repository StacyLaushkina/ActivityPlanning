package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.ui.DashboardPresenter
import com.laushkina.activityplanning.ui.DashboardView
import dagger.Module
import dagger.Provides

@Module(includes = [DashboardViewModule::class, ServiceModule::class])
class DashboardPresenterModule {
    @Provides
    fun getDashboardPresenter(view: DashboardView, service: TrackService): DashboardPresenter {
        return DashboardPresenter(view, service)
    }
}