package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.ui.TrackPresenter
import com.laushkina.activityplanning.ui.TrackView
import dagger.Module
import dagger.Provides

@Module(includes = [TrackViewModule::class, ServiceModule::class])
class TrackPresenterModule {
    @Provides
    fun getTrackPresenter(view: TrackView, service: TrackService): TrackPresenter {
        return TrackPresenter(view, service)
    }
}