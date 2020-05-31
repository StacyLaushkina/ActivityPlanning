package com.laushkina.activityplanning.ui.track

import com.laushkina.activityplanning.model.track.Track

interface TrackItemView {
    fun enableStartButton(holder: TrackAdapter.ViewHolder, position: Int, track: Track)

    fun enableEndButton(holder: TrackAdapter.ViewHolder, position: Int, track: Track)

    fun enableContinueButton(holder: TrackAdapter.ViewHolder, position: Int, track: Track)

    fun setActivityName(holder: TrackAdapter.ViewHolder, name: String)

    fun showProgress(holder: TrackAdapter.ViewHolder, time: String?)
}