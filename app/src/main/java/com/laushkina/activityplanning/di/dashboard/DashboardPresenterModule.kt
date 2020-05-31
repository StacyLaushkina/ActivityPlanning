package com.laushkina.activityplanning.di.dashboard

import com.laushkina.activityplanning.di.ServiceModule
import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.ui.dashboard.DashboardPresenter
import com.laushkina.activityplanning.ui.dashboard.DashboardView
import dagger.Module
import dagger.Provides

@Module(includes = [DashboardViewModule::class, ServiceModule::class])
class DashboardPresenterModule {
    @Provides
    fun getDashboardPresenter(view: DashboardView, service: TrackService): DashboardPresenter {
        return DashboardPresenter(view, service)
    }
}