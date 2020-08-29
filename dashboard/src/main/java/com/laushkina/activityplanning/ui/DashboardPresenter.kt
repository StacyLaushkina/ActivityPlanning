package com.laushkina.activityplanning.ui

import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import io.reactivex.disposables.CompositeDisposable
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DashboardPresenter(private val view: DashboardView, private val service: TrackService) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dashboardColors = DashboardColors()
    private val dateFormat = "dd MMM yyyy"

    fun onCreate() {
        loadTracks(Date())
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onDateChangeRequested() {
        view.openDateSelection(Date().time)
    }

    fun onDateChange(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = GregorianCalendar(year, monthOfYear, dayOfMonth).time
        loadTracks(date)
    }

    private fun mapToPieSlices(tracks: List<Track>): List<PieModel> {
        val result = mutableListOf<PieModel>()
        for (track in tracks) {
            if (track.startTime != null) {
                val timeDiff = TrackService.getTimeDiff(track)

                result.add(PieModel(
                        track.plan.name,
                        TimeUnit.MILLISECONDS.toMinutes(timeDiff).toFloat(),
                        dashboardColors.getNextColor()
                ))
            }
        }
        return result.toList()
    }

    private fun showDate(date: Date) {
        val formatter = SimpleDateFormat(dateFormat, Locale.US)
        view.setDate(formatter.format(date))
    }

    private fun loadTracks(date: Date) {
        compositeDisposable.add(service.getTracks(date)
            .map { tracks: List<Track> -> mapToPieSlices(tracks) }
            .subscribe(
                { pieSlices: List<PieModel> ->
                    view.showChart(pieSlices)
                    showDate(date)
                },
                { throwable: Throwable -> view.showError(throwable.message)}
            ))
    }
}