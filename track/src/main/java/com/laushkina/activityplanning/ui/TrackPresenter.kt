package com.laushkina.activityplanning.ui

import android.os.CountDownTimer
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import com.laushkina.activityplanning.component.track.R
import com.laushkina.activityplanning.model.Utils
import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import io.reactivex.disposables.CompositeDisposable
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class TrackPresenter(
    private val view: TrackView,
    private val service: TrackService,
    private val planService: PlanService
) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    private val today = Date()

    private var timer: CountDownTimer? = null
    private var state: State = State.createEmpty()

    companion object {
        const val EPS_MILLIS = 30_000 // 30 seconds
        const val TIMER_MAX = 3_600_000L // 1 hour
        const val TIMER_STEP = 1_000L // 1 second
    }

    fun init() {
        compositeDisposable.add(
            planService.hasPlans().subscribe(
                { hasPlans: Boolean ->
                    if (hasPlans) {
                        loadTracks(today)
                    } else {
                        // If there are no plans, there is no point in loading tracks
                        updateView(Status.NO_PLANS, today)
                    }
                },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
    }

    fun onDestroy() {
        compositeDisposable.clear()
        timer?.cancel()
    }

    fun onDateChangeRequested() {
        view.openDateSelection(today.time)
    }

    fun onDateChange(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = GregorianCalendar(year, monthOfYear, dayOfMonth).time
        loadTracks(date)
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
                        view.showError(getString(R.string.no_plans_error))
                    }
                    saveState(tracks)
                    updateView(Status.IN_PROGRESS, today)
                    initAndStartTimer()
                },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
    }

    // Is called when tracking for today should be finished.
    fun onEndTrackingRequested() {
        val message = StringBuilder()
        for (track in state.tracks) {
            val timeSpent = TrackService.getTimeDiff(track)
            val diff = timeSpent - TrackService.getPlanningTimeMillis(track.plan)
            if (diff > EPS_MILLIS) {
                message.append(track.plan.name + getString(R.string.result_done_expired))
            } else if (diff <= EPS_MILLIS && diff >= -EPS_MILLIS) {
                message.append(track.plan.name + getString(R.string.result_done_in_time))
            } else {
                message.append(track.plan.name + getString(R.string.result_not_done))
            }
            message.append("\n")
        }
        view.showPopupMessage(message.toString())


        compositeDisposable.add(
            service.finishTracking(today).subscribe(
                { tracks: List<Track> ->
                    saveState(tracks)
                    updateView(Status.FINISHED, today)
                    timer?.cancel()
                },
                { throwable: Throwable -> view.showError(throwable.message) }
            ))
    }

    fun onCreatePlansRequested() {
        view.openPlansScreen()
    }

    @VisibleForTesting
    fun initAndStartTimer() {
        timer = object : CountDownTimer(TIMER_MAX, TIMER_STEP) {
            override fun onTick(millisUntilFinished: Long) {
                // Update progress
                view.updateTimes()
            }

            override fun onFinish() {}
        }
        timer?.start()
    }

    @VisibleForTesting
    fun saveState(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            this.state = State.createEmpty()
            return
        }

        val unfinishedTracks = tracks.binarySearch { if (it.isFinished) 1 else 0 }
        this.state = State(unfinishedTracks == -1, tracks)
    }

    @VisibleForTesting
    fun loadTracks(date: Date) {
        compositeDisposable.add(service.getAllTracks(date).subscribe(
            { tracks: List<Track> ->
                saveState(tracks)
                val isSameDay = Utils.isSameDay(date, today)
                val status = if (tracks.isEmpty()) {
                    if (isSameDay) {
                        Status.NOT_STARTED_TODAY
                    } else {
                        Status.NO_TRACKING
                    }
                } else {
                    if (state.trackingFinished) {
                        Status.FINISHED
                    } else {
                        Status.IN_PROGRESS
                    }
                }
                updateView(status, date)
                if (!state.trackingFinished && isSameDay) {
                    initAndStartTimer()
                }
            },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    @VisibleForTesting
    fun updateView(status: Status, date: Date) {
        when (status) {
            Status.NO_PLANS -> {
                view.hideDate()
                view.showInlineMessage(getString(R.string.plans_are_not_formed))
                view.showCreatePlansButton()
                view.hideTracks()
            }
            Status.NOT_STARTED_TODAY -> {
                showDate(date)
                view.showInlineMessage(getString(R.string.tracking_not_started_today))
                view.showStartTrackingButton()
                view.hideTracks()
            }
            Status.NO_TRACKING -> {
                showDate(date)
                view.showInlineMessage(getString(R.string.no_tracking_this_day))
                view.showStartTrackingButton()
                view.hideTracks()
            }
            Status.IN_PROGRESS -> {
                showDate(date)
                view.hideInlineMessage()
                view.showTracks(state.tracks, !state.trackingFinished)
                view.showEndTrackingButton()
            }
            Status.FINISHED -> {
                showDate(date)
                view.hideInlineMessage()
                view.showTracks(state.tracks, !state.trackingFinished)
                view.hideTrackingButton()
            }
        }
    }

    private fun getString(@StringRes id: Int): String {
        return view.getContext().getString(id)
    }

    private fun updateTrack(track: Track) {
        compositeDisposable.add(
            service.updateTrack(track).subscribe(
                { tracks: List<Track> -> saveState(tracks); updateView(Status.IN_PROGRESS, today) },
                { throwable: Throwable -> view.showError(throwable.message) }
            ))
    }

    private fun showDate(date: Date) {
        view.showDate(formatter.format(date))
    }

    enum class Status {
        NO_PLANS,
        NOT_STARTED_TODAY,
        NO_TRACKING,
        IN_PROGRESS,
        FINISHED
    }

    private class State(val trackingFinished: Boolean, val tracks: List<Track>) {
        companion object {
            fun createEmpty(): State {
                return State(false, emptyList())
            }
        }
    }
}