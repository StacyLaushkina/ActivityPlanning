package com.laushkina.activityplanning.ui

import android.graphics.Color
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import java.util.concurrent.TimeUnit

class TrackItemPresenter(private val view: TrackItemView, private val tracks: List<Track>) {

    fun getItemCount(): Int {
        return tracks.size
    }

    fun onBindViewHolder(holder: TrackAdapter.ViewHolder, position: Int) {
        val track = tracks[position]

        view.setActivityName(holder, track.plan.activityName)

        if (track.isInProgress) {
            view.showStopButton(holder, position, track)
        } else {
            view.showStartButton(holder, position, track)
        }

        val progress = getProgress(track.startTime, track.duration, track.isInProgress)
        val color = if (progress < 100) {
            if (progress < 95) Color.GREEN else Color.YELLOW
        } else {
            if (progress < 105) Color.YELLOW else Color.RED
        }
        view.showProgress(holder, progress, "$progress%", color)
    }

    private fun getProgress(startTime: Long?, duration: Long, isInProgress: Boolean): Int {
        if (startTime == null) {
            return 0
        }
        val diff = TrackService.getTimeDiff(startTime, duration, isInProgress)
        return (diff * 100 / (TimeUnit.MINUTES.toMillis(20))).toInt()
    }
}