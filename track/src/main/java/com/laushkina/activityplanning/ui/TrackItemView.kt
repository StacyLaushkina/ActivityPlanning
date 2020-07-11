package com.laushkina.activityplanning.ui

import com.laushkina.activityplanning.model.track.Track

interface TrackItemView {
    fun showStartButton(holder: TrackAdapter.ViewHolder, position: Int, track: Track)

    fun showStopButton(holder: TrackAdapter.ViewHolder, position: Int, track: Track)

    fun setActivityName(holder: TrackAdapter.ViewHolder, name: String)

    fun showProgress(holder: TrackAdapter.ViewHolder, progress: Int, text: String, color: Int)
}
