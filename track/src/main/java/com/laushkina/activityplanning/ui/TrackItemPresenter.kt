package com.laushkina.activityplanning.ui

import android.graphics.Color
import com.laushkina.activityplanning.component.track.R
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import java.util.concurrent.TimeUnit

class TrackItemPresenter(private val view: TrackItemView,
                         private val tracks: List<Track>,
                         private val showControlButtons: Boolean) {

    fun getItemCount(): Int {
        return tracks.size
    }

    fun onBindViewHolder(holder: TrackAdapter.ViewHolder, position: Int) {
        val track = tracks[position]

        view.setActivityName(holder, track.plan.name)

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
        val resources = holder.progress.context.resources
        val color = if (progress < 100) {
            if (progress < 95) resources.getColor(R.color.progress_green_color)
            else resources.getColor(R.color.progress_green_yellow)
        } else {
            if (progress < 105) resources.getColor(R.color.progress_green_yellow)
            else resources.getColor(R.color.progress_green_red)
        }
        val textProgress = getFullProgress(track)
        view.showProgress(holder, progress, "$textProgress ($progress%)", color)
    }

    private fun getProgress(track: Track): Int {
        val diff = TrackService.getTimeDiff(track)
        return (diff * 100 / TrackService.getPlanningTimeMillis(track.plan)).toInt()
    }

    private fun getFullProgress(track: Track): String {
        val diff = TimeUnit.MILLISECONDS.toMinutes(TrackService.getTimeDiff(track))
        val plan = TimeUnit.MILLISECONDS.toMinutes(TrackService.getPlanningTimeMillis(track.plan))
        return "$diff/$plan min"
    }
}