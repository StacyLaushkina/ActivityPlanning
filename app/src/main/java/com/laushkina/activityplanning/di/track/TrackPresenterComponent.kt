package com.laushkina.activityplanning.di.track

import com.laushkina.activityplanning.ui.track.TrackPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TrackPresenterModule::class])
interface TrackPresenterComponent {
    fun getTrackPresenter(): TrackPresenter
}