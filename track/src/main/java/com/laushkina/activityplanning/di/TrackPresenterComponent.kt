package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.ui.TrackPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TrackPresenterModule::class])
interface TrackPresenterComponent {
    fun getTrackPresenter(): TrackPresenter
}