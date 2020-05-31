package com.laushkina.activityplanning.di.track

import com.laushkina.activityplanning.ui.track.TrackView
import dagger.Module
import dagger.Provides

@Module
class TrackViewModule(private val view: TrackView) {
    @Provides
    fun tracksView(): TrackView {
        return view
    }
}