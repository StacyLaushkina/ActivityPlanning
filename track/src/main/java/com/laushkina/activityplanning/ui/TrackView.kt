package com.laushkina.activityplanning.ui

import com.laushkina.activityplanning.model.track.Track

interface TrackView {
    fun showTracks(tracks: List<Track>, date: String)
    fun showError(message: String?)
    fun showMessage(message: String)

    fun showStartTrackingButton()
    fun hideStartTrackingButton()

    fun showEndTrackingButton()
    fun hideEndTrackingButton()
}