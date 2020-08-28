package com.laushkina.activityplanning.ui

import android.os.CountDownTimer
import androidx.annotation.VisibleForTesting
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import io.reactivex.MaybeObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TrackPresenter(private val view: TrackView, private val service: TrackService) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val eps = TimeUnit.SECONDS.toMillis(30)
    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    private val timerMaxValue = TimeUnit.HOURS.toMillis(1)
    private val timerStep = TimeUnit.SECONDS.toMillis(10)

    private var timer: CountDownTimer? = null
    private lateinit var tracks: List<Track>

    fun onCreate() {
        val today = Date()
        loadTracks(today)
    }

    fun onDateChangeRequested() {
        view.openDateSelection(Date().time)
    }

    fun onDateChange(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = GregorianCalendar(year, monthOfYear, dayOfMonth).time
        loadTracks(date)
    }

    fun onDestroy() {
        compositeDisposable.clear()
        timer?.cancel()
    }

    // Is called when individual track was requested to start
    fun onTrackStart(track: Track) {
        track.startTime = System.currentTimeMillis()
        track.isInProgress = true
        updateTrack(track)
    }

    // Is called when individual track was requested to stop
    fun onTrackStop(track: Track) {
        if (track.startTime == null) {
            throw RuntimeException("Attempt to end time with empty start")
        }

        track.startTime.let {
            track.duration += System.currentTimeMillis() - track.startTime!!
            track.isInProgress = false
            updateTrack(track)
        }
    }

    // Is called when tracking for today is needed
    fun onStartTracksForToday() {
        compositeDisposable.add(
            service.startTracking().subscribe(
                { tracks: List<Track> ->
                    if (tracks.isEmpty()) {
                        view.showError("There are no plans. Please create them first.")
                    }
                    onTracksLoaded(tracks)
                },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
    }

    fun onEndTrackingRequested() {
        val message = StringBuilder()
        for (track in this.tracks) {
            val timeSpent = TrackService.getTimeDiff(track)
            val diff = timeSpent - TrackService.getPlanningTimeMillis(track.plan)
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
        view.hideStartTrackingButton()
        view.hideEndTrackingButton()

        compositeDisposable.add(
            service.finishTracking(Date()).subscribe(
            { tracks: List<Track> ->onTracksLoaded(tracks) },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    @VisibleForTesting
    fun initAndStartTimer() {
        timer = object : CountDownTimer(timerMaxValue, timerStep) {
            override fun onTick(millisUntilFinished: Long) {
                view.updateTimes()
            }

            override fun onFinish() {}
        }
        timer?.start()
    }

    @VisibleForTesting
    fun onTracksLoaded(tracks: List<Track>) {
        this.tracks = tracks
        if (tracks.isEmpty()) {
            return
        }

        val unfinishedTracks = tracks.binarySearch { if (it.isFinished) 1 else 0 }
        val finished = unfinishedTracks == -1
        if (!finished) {
            initAndStartTimer()
        }

        view.hideStartTrackingButton()
        view.showTracks(tracks, !finished)
        if (finished) {
            view.hideEndTrackingButton()
        } else {
            view.showEndTrackingButton()
        }
    }

    @VisibleForTesting
    fun loadTracks(date: Date) {
        compositeDisposable.add(service.getUnfinishedTracks(date).subscribe(
            { tracks: List<Track> ->
                onTracksLoaded(tracks)
                setDate(date)
                if (tracks.isEmpty()) {
                    view.showStartTrackingButton()
                }
            },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    private fun updateTrack(track: Track) {
        compositeDisposable.add(
            service.updateTrack(track).subscribe(
                { tracks: List<Track> ->onTracksLoaded(tracks) },
                { throwable: Throwable -> view.showError(throwable.message) }
            ))
    }

    private fun setDate(date: Date) {
        view.showDate(formatter.format(date))
    }
}