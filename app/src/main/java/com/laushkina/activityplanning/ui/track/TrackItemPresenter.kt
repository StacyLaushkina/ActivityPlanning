package com.laushkina.activityplanning.ui.track

import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import java.util.concurrent.TimeUnit

class TrackItemPresenter(private val view: TrackItemView, private val tracks: List<Track>) {

    fun getItemCount(): Int {
        return tracks.size
    }

    fun onBindViewHolder(holder: TracksAdapter.ViewHolder, position: Int) {
        val track = tracks[position]

        view.setActivityName(holder, track.plan.activityName)

        when(getTrackState(track.startTime, track.endTime)) {
            TrackState.NOT_STARTED -> {
                view.enableStartButton(holder, position, track)
            }
            TrackState.IN_PROGRESS -> {
                view.showProgress(holder, getProgress(track.startTime, track.endTime))
                view.enableEndButton(holder, position, track)
            }
            TrackState.STOPPED -> {
                view.showProgress(holder, getProgress(track.startTime, track.endTime))
                view.enableContinueButton(holder, position, track)
            }
        }
    }

    private fun getProgress(startTime: Long?, endTime: Long?): String? {
        if (startTime == null) {
            return null
        }
        val diff = TrackService.getTimeDiff(startTime, endTime)
        return TimeUnit.MILLISECONDS.toMinutes(diff).toString() + " min"
    }

    private fun getTrackState(startTime: Long?, endTime: Long?): TrackState {
        if (startTime == null && endTime == null) {
            return TrackState.NOT_STARTED
        }

        if (startTime != null && endTime == null) {
            return TrackState.IN_PROGRESS
        }

        if (startTime != null && endTime != null) {
            return TrackState.STOPPED
        }

        // Default state
        return TrackState.NOT_STARTED
    }

    private enum class TrackState {
        NOT_STARTED,
        IN_PROGRESS,
        STOPPED
    }
}