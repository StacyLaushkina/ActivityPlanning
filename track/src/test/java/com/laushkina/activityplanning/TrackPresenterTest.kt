package com.laushkina.activityplanning

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
    private val view = mock<TrackView>()
    private val service = mock<TrackService> {
        doReturn(Maybe.just(emptyList<Track>())).`when`(mock).updateTrack(any())
    }

    private val presenter = TrackPresenter(view, service)

    @Test
    fun `show today date and load unfinished tracks for today when onCreate()`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getUnfinishedTracks(any())

        presenter.onCreate()

        val today = Date()
        val format = SimpleDateFormat("dd MMM yyyy", Locale.US)
        verify(view).showDate(format.format(today))

        val responseCaptor = argumentCaptor<Date>()
        verify(service).getUnfinishedTracks(responseCaptor.capture())
        assertDatesAreForTheSameDay(today, responseCaptor.firstValue)
    }

    @Test
    fun `open date selection when date change requested`() {
        presenter.onDateChangeRequested()

        val responseCaptor = argumentCaptor<Long>()
        verify(view).openDateSelection(responseCaptor.capture())
        assertDatesAreForTheSameDay(Date(), Date(responseCaptor.firstValue))
    }

    @Test
    fun `update start time and mark track as in progress when start track requested`() {
        val yesterday = Date().time - TimeUnit.DAYS.toMillis(1)
        val track = createNewTrack(Date(yesterday))

        presenter.onTrackStart(track)

        assertTrue(track.isInProgress)
        assertDatesAreForTheSameDay(Date(), Date(track.startTime!!))

        verify(service).updateTrack(track)
    }

    @Test(expected = RuntimeException::class)
    fun `throw exception when try to finish track before starting it`() {
        val track = createNewTrack(Date())
        presenter.onTrackStop(track)
    }

    @Test
    fun `update duration time and pause track when request stop it`() {
        val track = createNewTrack(Date())
        val durationBefore = track.duration

        presenter.onTrackStart(track)
        presenter.onTrackStop(track)

        assertTrue(track.duration - durationBefore > 0)
        assertFalse(track.isInProgress)

        verify(service, times(2)).updateTrack(track)
    }

    @Test
    fun `start tracking for today when it is requested`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).startTracking()

        presenter.onStartTracksForToday()
        verify(service).startTracking()
    }

    @Test
    fun `do not start timer and hide end button if there is finished track`() {
        val tracks = listOf(createFinishedTrack(Date()), createNewTrack(Date()))

        val presenterSpy = spy(presenter)
        presenterSpy.onTracksLoaded(tracks)

        verify(presenterSpy, never()).initAndStartTimer()
        verify(view).hideEndTrackingButton()
        verify(view).hideStartTrackingButton()
        verify(view, never()).showEndTrackingButton()
        verify(view).showTracks(tracks, false)
    }

    @Test
    fun `start timer and show end button if there are no finished tracks`() {
        val tracks = listOf(createNewTrack(Date()), createNewTrack(Date()))

        val presenterSpy = spy(presenter) {
            doNothing().`when`(mock).initAndStartTimer()
        }
        presenterSpy.onTracksLoaded(tracks)

        verify(presenterSpy).initAndStartTimer()
        verify(view).showEndTrackingButton()
        verify(view).hideStartTrackingButton()
        verify(view, never()).hideEndTrackingButton()
        verify(view).showTracks(tracks, true)
    }

    @Test
    fun `show start tracking button when there are no tracks`() {
        doReturn(Maybe.just(emptyList<Track>())).`when`(service).getUnfinishedTracks(any())

        val presenterSpy = spy(presenter)
        presenterSpy.loadTracks(Date())

        verify(presenterSpy).onTracksLoaded(any())
        verify(view).showDate(any())
        verify(view).showStartTrackingButton()
    }

    private fun assertDatesAreForTheSameDay(date1: Date, date2: Date) {
        assertEquals(
            TimeUnit.MILLISECONDS.toDays(date1.time),
            TimeUnit.MILLISECONDS.toDays(date2.time)
        )
    }

    private fun createNewTrack(creationDate: Date): Track {
        return createFinishedTrack(creationDate, false)
    }

    private fun createFinishedTrack(creationDate: Date): Track {
        return createFinishedTrack(creationDate, true)
    }

    private fun createFinishedTrack(creationDate: Date, isFinished: Boolean): Track {
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