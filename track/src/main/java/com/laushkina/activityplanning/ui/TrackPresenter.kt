package com.laushkina.activityplanning.ui

import android.os.CountDownTimer
import androidx.annotation.VisibleForTesting
import com.laushkina.activityplanning.model.Utils
import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import io.reactivex.disposables.CompositeDisposable
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TrackPresenter(
    private val view: TrackView,
    private val service: TrackService,
    private val planService: PlanService
) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val eps = TimeUnit.SECONDS.toMillis(30)
    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    private val timerMaxValue = TimeUnit.HOURS.toMillis(1)
    private val timerStep = TimeUnit.SECONDS.toMillis(10)
    private val today = Date()

    private var timer: CountDownTimer? = null
    private var trackingFinished = true
    private lateinit var tracks: List<Track>

    fun init() {
        compositeDisposable.add(
            planService.hasPlans().subscribe(
                { hasPlans: Boolean ->
                    if (hasPlans) {
                        loadTracks(today)
                    } else {
                        updateView(State.NO_PLANS, today)
                    }
                },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
    }

    fun onDateChangeRequested() {
        view.openDateSelection(today.time)
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
                    saveTracks(tracks)
                    updateView(State.IN_PROGRESS, today)
                    initAndStartTimer()
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
                message.append(track.plan.name + ":" + "done! But took a bit more.")
            } else if (diff <= eps && diff >= -eps) {
                message.append(track.plan.name + ":" + "done!")
            } else {
                message.append(track.plan.name + ":" + "not done. Should spend more time.")
            }
            message.append("\n")
        }
        view.showPopupMessage(message.toString())


        compositeDisposable.add(
            service.finishTracking(today).subscribe(
                { tracks: List<Track> ->
                    saveTracks(tracks)
                    updateView(State.FINISHED, today)
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
        timer = object : CountDownTimer(timerMaxValue, timerStep) {
            override fun onTick(millisUntilFinished: Long) {
                view.updateTimes()
            }

            override fun onFinish() {}
        }
        timer?.start()
    }

    @VisibleForTesting
    fun saveTracks(tracks: List<Track>) {
        this.tracks = tracks
        if (tracks.isEmpty()) {
            return
        }

        val unfinishedTracks = tracks.binarySearch { if (it.isFinished) 1 else 0 }
        this.trackingFinished = unfinishedTracks == -1
    }

    @VisibleForTesting
    fun loadTracks(date: Date) {
        compositeDisposable.add(service.getAllTracks(date).subscribe(
            { tracks: List<Track> ->
                saveTracks(tracks)
                val isSameDay = Utils.isSameDay(date, today)
                val state = if (tracks.isEmpty()) {
                    if (isSameDay) {
                        State.NOT_STARTED_TODAY
                    } else {
                        State.NO_TRACKING
                    }
                } else {
                    if (trackingFinished) {
                        State.FINISHED
                    } else {
                        State.IN_PROGRESS
                    }
                }
                updateView(state, date)
                if (!trackingFinished && isSameDay) {
                    initAndStartTimer()
                }
            },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    @VisibleForTesting
    fun updateView(state: State, date: Date) {
        when (state) {
            State.NO_PLANS -> {
                view.hideDate()
                view.showInlineMessage("Plans are not formed.")
                view.showCreatePlansButton()
                view.hideTracks()
            }
            State.NOT_STARTED_TODAY -> {
                showDate(date)
                view.showInlineMessage("Not started today")
                view.showStartTrackingButton()
                view.hideTracks()
            }
            State.NO_TRACKING -> {
                showDate(date)
                view.showInlineMessage("No tracks this day")
                view.showStartTrackingButton()
                view.hideTracks()
            }
            State.IN_PROGRESS -> {
                showDate(date)
                view.hideInlineMessage()
                view.showTracks(tracks, !trackingFinished)
                view.showEndTrackingButton()
            }
            State.FINISHED -> {
                showDate(date)
                view.hideInlineMessage()
                view.showTracks(tracks, !trackingFinished)
                view.hideTrackingButton()
            }
        }
    }

    private fun updateTrack(track: Track) {
        compositeDisposable.add(
            service.updateTrack(track).subscribe(
                { tracks: List<Track> -> saveTracks(tracks); updateView(State.IN_PROGRESS, today) },
                { throwable: Throwable -> view.showError(throwable.message) }
            ))
    }

    private fun showDate(date: Date) {
        view.showDate(formatter.format(date))
    }

    enum class State {
        NO_PLANS,
        NOT_STARTED_TODAY,
        NO_TRACKING,
        IN_PROGRESS,
        FINISHED
    }
}