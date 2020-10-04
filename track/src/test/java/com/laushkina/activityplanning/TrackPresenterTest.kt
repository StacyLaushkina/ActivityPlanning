package com.laushkina.activityplanning

import android.content.Context
import com.laushkina.activityplanning.model.Utils
import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.ui.TrackPresenter
import com.laushkina.activityplanning.ui.TrackView
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Maybe
import junit.framework.TestCase.*
import org.junit.Test
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TrackPresenterTest {
    val context = mock<Context>()
    private val view = mock<TrackView> {
        doReturn(context).`when`(mock).getContext()
    }
    private val service = mock<TrackService> {
        doReturn(Maybe.just(emptyList<Track>())).`when`(mock).updateTrack(any())
    }
    private val planService = mock<PlanService> {
        doReturn(Maybe.just(true)).`when`(mock).hasPlans()
    }

    private val presenter = TrackPresenter(view, service, planService)

    @Test
    fun `show today date and load unfinished tracks for today when onCreate()`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getAllTracks(any())

        presenter.init()

        val today = Date()
        val format = SimpleDateFormat("dd MMM yyyy", Locale.US)
        verify(view).showDate(format.format(today))

        val responseCaptor = argumentCaptor<Date>()
        verify(service).getAllTracks(responseCaptor.capture())
        assertTrue(Utils.isSameDay(today, responseCaptor.firstValue))
    }

    @Test
    fun `open date selection when date change requested`() {
        presenter.onDateChangeRequested()

        val responseCaptor = argumentCaptor<Long>()
        verify(view).openDateSelection(responseCaptor.capture())
        assertTrue(Utils.isSameDay(Date(), Date(responseCaptor.firstValue)))
        verifyNoMoreInteractions(view)
    }

    @Test
    fun `update start time and mark track as in progress when start track requested`() {
        val yesterday = Date().time - TimeUnit.DAYS.toMillis(1)
        val track = createTrack(Date(yesterday), false)

        presenter.onTrackStart(track)

        assertTrue(track.isInProgress)
        assertTrue(Utils.isSameDay(Date(), Date(track.startTime!!)))

        verify(service).updateTrack(track)
        verifyNoMoreInteractions(service)
    }

    @Test(expected = RuntimeException::class)
    fun `throw exception when try to finish track before starting it`() {
        val track = createTrack(Date(), false)
        presenter.onTrackStop(track)
    }

    @Test
    fun `update duration time and pause track when request stop it`() {
        val track = createTrack(Date(), false)
        val durationBefore = track.duration

        presenter.onTrackStart(track)
        presenter.onTrackStop(track)

        assertTrue(track.duration - durationBefore > 0)
        assertFalse(track.isInProgress)

        verify(service, times(2)).updateTrack(track)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun `start tracking for today when it is requested`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).startTracking()

        presenter.onStartTracksForToday()
        verify(service).startTracking()
    }

    @Test
    fun `set view to NO_PLANS state is correct`() {
        doReturn("").`when`(context).getString(any())
        presenter.updateView(TrackPresenter.Status.NO_PLANS, Date())

        verify(view).hideDate()
        verify(view).showInlineMessage(any())
        verify(view).showCreatePlansButton()
        verify(view).hideTracks()
        verify(view).getContext()

        verifyNoMoreInteractions(view)
    }

    @Test
    fun `set view to NOT_STARTED_TODAY state is correct`() {
        doReturn("").`when`(context).getString(any())
        presenter.updateView(TrackPresenter.Status.NOT_STARTED_TODAY, Date())

        verify(view).showDate(any())
        verify(view).showInlineMessage(any())
        verify(view).showStartTrackingButton()
        verify(view).hideTracks()
        verify(view).getContext()

        verifyNoMoreInteractions(view)
    }

    @Test
    fun `set view to NO_TRACKING state is correct`() {
        doReturn("").`when`(context).getString(any())
        presenter.updateView(TrackPresenter.Status.NO_TRACKING, Date())

        verify(view).showDate(any())
        verify(view).showInlineMessage(any())
        verify(view).showStartTrackingButton()
        verify(view).hideTracks()
        verify(view).getContext()

        verifyNoMoreInteractions(view)
    }

    @Test
    fun `set view to IN_PROGRESS state is correct`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getAllTracks(any())
        presenter.init()
        presenter.updateView(TrackPresenter.Status.IN_PROGRESS, Date())

        verify(view, times(2)).showDate(any())
        verify(view).hideInlineMessage()
        verify(view).showTracks(any(), any())
        verify(view).showEndTrackingButton()

    }

    @Test
    fun `set view to FINISHED state is correct`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getAllTracks(any())
        presenter.init()
        presenter.updateView(TrackPresenter.Status.FINISHED, Date())

        verify(view, times(2)).showDate(any())
        view.hideInlineMessage()
        verify(view).showTracks(any(), any())
        view.hideTrackingButton()
    }

    @Test
    fun `when there are no tracks and date is today, state is NOT_STARTED_TODAY`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getAllTracks(any())

        val today = Date()
        val presenterSpy = spy(presenter)
        presenterSpy.loadTracks(today)
        verify(presenterSpy).updateView(TrackPresenter.Status.NOT_STARTED_TODAY, today)
    }

    @Test
    fun `when there are no tracks and date is another day, state is NO_TRACKING`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getAllTracks(any())

        val beforeYesterday = Date(Date().time - TimeUnit.DAYS.toMillis(2))
        val presenterSpy = spy(presenter)
        presenterSpy.loadTracks(beforeYesterday)
        verify(presenterSpy).updateView(TrackPresenter.Status.NO_TRACKING, beforeYesterday)
    }

    @Test
    fun `when there are only finished tasks, state is NO_TRACKING`() {
        val beforeYesterday = Date(Date().time - TimeUnit.DAYS.toMillis(2))

        val tracks = listOf(createTrack(beforeYesterday, true), createTrack(beforeYesterday, true))
        doReturn(Maybe.just(tracks)).`when`(service).getAllTracks(any())

        val presenterSpy = spy(presenter)

        presenterSpy.loadTracks(beforeYesterday)
        verify(presenterSpy).updateView(TrackPresenter.Status.FINISHED, beforeYesterday)
    }

    @Test
    fun `when there are only unfinished tracks, state is IN_PROGRESS`() {
        val beforeYesterday = Date(Date().time - TimeUnit.DAYS.toMillis(2))

        val tracks = listOf(createTrack(beforeYesterday, false), createTrack(beforeYesterday, false))
        doReturn(Maybe.just(tracks)).`when`(service).getAllTracks(any())

        val presenterSpy = spy(presenter)

        presenterSpy.loadTracks(beforeYesterday)
        verify(presenterSpy).updateView(TrackPresenter.Status.IN_PROGRESS, beforeYesterday)
    }

    @Test
    fun `when there are finished and unfinished tasks, state is FINISHED`() {
        val beforeYesterday = Date(Date().time - TimeUnit.DAYS.toMillis(2))

        val tracks = listOf(createTrack(beforeYesterday, true), createTrack(beforeYesterday, false))
        doReturn(Maybe.just(tracks)).`when`(service).getAllTracks(any())

        val presenterSpy = spy(presenter)

        presenterSpy.loadTracks(beforeYesterday)
        verify(presenterSpy).updateView(TrackPresenter.Status.FINISHED, beforeYesterday)
    }

    @Test
    fun `init start timer only if date is today and tracking is not finished`() {
        val tracks = listOf(createTrack(Date(), false), createTrack(Date(), false))
        doReturn(Maybe.just(tracks)).`when`(service).getAllTracks(any())

        val presenterSpy = spy(presenter)  {
            doNothing().`when`(mock).initAndStartTimer()
        }

        presenterSpy.loadTracks(Date())

        verify(presenterSpy).initAndStartTimer()
    }

    @Test
    fun `do not init start timer if date is not today`() {
        val beforeYesterday = Date(Date().time - TimeUnit.DAYS.toMillis(2))

        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getAllTracks(any())
        val presenterSpy = spy(presenter)

        presenterSpy.loadTracks(beforeYesterday)

        verify(presenterSpy, never()).initAndStartTimer()
    }

    @Test
    fun `do not init start timer if tracking is finished`() {
        val tracks = listOf(createTrack(Date(), true))
        doReturn(Maybe.just(tracks)).`when`(service).getAllTracks(any())

        val presenterSpy = spy(presenter)

        presenterSpy.loadTracks(Date())

        verify(presenterSpy, never()).initAndStartTimer()
    }

    private fun createTrack(creationDate: Date, isFinished: Boolean): Track {
        return Track(
            0,
            mock(),
            null,
            0,
            isInProgress = false,
            isFinished = isFinished,
            date = creationDate
        )
    }
}