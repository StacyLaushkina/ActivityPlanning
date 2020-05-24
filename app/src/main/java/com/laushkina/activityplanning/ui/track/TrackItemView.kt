package com.laushkina.activityplanning.ui.track

import com.laushkina.activityplanning.model.track.Track

interface TrackItemView {
    fun enableStartButton(holder: TracksAdapter.ViewHolder, position: Int, track: Track)

    fun enableEndButton(holder: TracksAdapter.ViewHolder, position: Int, track: Track)

    fun enableContinueButton(holder: TracksAdapter.ViewHolder, position: Int, track: Track)

    fun setActivityName(holder: TracksAdapter.ViewHolder, name: String)

    fun showProgress(holder: TracksAdapter.ViewHolder, time: String?)
}