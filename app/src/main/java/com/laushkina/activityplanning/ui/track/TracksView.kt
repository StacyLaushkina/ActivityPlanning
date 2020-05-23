package com.laushkina.activityplanning.ui.track

import com.laushkina.activityplanning.model.track.Track

interface TracksView {
    fun showTracks(tracks: List<Track>)
    fun showError(message: String?)
}