package com.laushkina.activityplanning.ui

import com.laushkina.activityplanning.model.track.Track

interface TrackView {
    fun showTracks(tracks: List<Track>, showControlButtons: Boolean)
    fun hideTracks()

    fun showDate(date: String)
    fun hideDate()

    fun showError(message: String?)
    fun showPopupMessage(message: String)

    fun showInlineMessage(message: CharSequence)
    fun hideInlineMessage()

    fun showStartTrackingButton()
    fun showEndTrackingButton()
    fun showCreatePlansButton()
    fun hideTrackingButton()

    fun updateTimes()
    fun openDateSelection(maxDate: Long)

    fun openPlansScreen()
}