package com.laushkina.activityplanning.ui

import android.graphics.Color
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService

class TrackItemPresenter(private val view: TrackItemView,
                         private val tracks: List<Track>,
                         private val showControlButtons: Boolean) {

    fun getItemCount(): Int {
        return tracks.size
    }

    fun onBindViewHolder(holder: TrackAdapter.ViewHolder, position: Int) {
        val track = tracks[position]

        view.setActivityName(holder, track.plan.activityName)

        if (showControlButtons) {
            if (track.isInProgress) {
                view.showStopButton(holder, position, track)
            } else {
                view.showStartButton(holder, position, track)
            }
        } else {
            view.hideControlButtons(holder)
        }

        val progress = getProgress(track)
        val color = if (progress < 100) {
            if (progress < 95) Color.GREEN else Color.YELLOW
        } else {
            if (progress < 105) Color.YELLOW else Color.RED
        }
        view.showProgress(holder, progress, "$progress%", color)
    }

    private fun getProgress(track: Track): Int {
        val diff = TrackService.getTimeDiff(track)
        return (diff * 100 / TrackService.getPlanningTimeMillis(track.plan)).toInt()
    }
}