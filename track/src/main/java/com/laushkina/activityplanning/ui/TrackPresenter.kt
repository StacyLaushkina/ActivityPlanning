package com.laushkina.activityplanning.ui

import android.os.CountDownTimer
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import io.reactivex.disposables.CompositeDisposable
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TrackPresenter(private val view: TrackView, private val service: TrackService) {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var tracks: List<Track>
    private lateinit var timer: CountDownTimer

    private val eps = TimeUnit.SECONDS.toMillis(30)
    private val dateFormat = "dd MMM yyyy"
    private val timerMaxValue = TimeUnit.HOURS.toMillis(1)
    private val timerStep = TimeUnit.SECONDS.toMillis(1)

    fun onCreate() {
        compositeDisposable.add(service.getTracks(Date()).subscribe(
            { tracks: List<Track> ->
                if (tracks.isEmpty()) {
                    view.showStartTrackingButton()
                } else {
                    onTracksLoaded(tracks)
                    initAndStartTimer()
                }
            },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    private fun initAndStartTimer() {
        timer = object : CountDownTimer(timerMaxValue, timerStep) {
            override fun onTick(millisUntilFinished: Long) {
                view.updateTimes()
            }

            override fun onFinish() {}
        }
        timer.start()
    }

    fun onDestroy() {
        compositeDisposable.clear()
        timer.cancel()
    }

    fun onTrackStart(track: Track) {
        track.startTime = System.currentTimeMillis()
        track.isInProgress = true
        updateTrack(track)
    }

    fun onTrackStop(track: Track) {
        if (track.startTime == null) {
            throw RuntimeException("Attempt to end time with empty start")
        }

        track.startTime.let {
            track.duration = track.duration + (System.currentTimeMillis() - track.startTime!!)
            track.isInProgress = false
            updateTrack(track)
        }
    }

    fun onStartTrackingRequested() {
        compositeDisposable.add(service.startTracking().subscribe(
            { tracks: List<Track> -> onTracksLoaded(tracks) },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    fun onEndTrackingRequested() {
        val message = StringBuilder()
        for (track in this.tracks) {
            val timeSpent = if (track.startTime != null) TrackService.getTimeDiff(
                track.startTime!!,
                track.duration,
                track.isInProgress
            ) else 0
            // TODO hours per date
            val diff = timeSpent - TimeUnit.HOURS.toMillis((track.plan.percent * 0.01 * 8).toLong())
            if (diff > eps) {
                message.append(track.plan.activityName + ":" + "done! But took a bit more.")
            } else if (diff <= eps && diff >= -eps) {
                message.append(track.plan.activityName + ":" + "done!")
            } else {
                message.append(track.plan.activityName + ":" + "not done. Should spend more time.")
            }
            message.append("\n")
        }
        view.showMessage(message.toString())
        view.showStartTrackingButton()
        view.hideEndTrackingButton()
    }

    private fun updateTrack(track: Track) {
        compositeDisposable.add(service.updateTrack(track).subscribe(
            { tracks: List<Track> -> onTracksLoaded(tracks) },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    private fun onTracksLoaded(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            this.tracks = tracks
            view.hideStartTrackingButton()

            val formatter = SimpleDateFormat(dateFormat, Locale.US)
            val dateString = formatter.format(tracks[0].date)

            view.showTracks(tracks, dateString)
            view.showEndTrackingButton()
        }

    }
}