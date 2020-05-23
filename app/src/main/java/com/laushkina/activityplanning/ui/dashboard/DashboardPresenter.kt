package com.laushkina.activityplanning.ui.dashboard

import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import io.reactivex.disposables.CompositeDisposable
import org.eazegraph.lib.models.PieModel
import java.util.concurrent.TimeUnit

class DashboardPresenter(private val view: DashboardView, private val service: TrackService) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dashboardColors = DashboardColors()

    fun onCreate() {
        compositeDisposable.add(service.getTracks()
            .map { tracks: List<Track> -> mapToPieSlices(tracks) }
            .subscribe(
                { pieSlices: List<PieModel> -> view.showChart(pieSlices)},
                { throwable: Throwable -> view.showError(throwable.message)}
        ))
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun mapToPieSlices(tracks: List<Track>): List<PieModel> {
        val result = mutableListOf<PieModel>()
        for (track in tracks) {
            if (track.endTime != null && track.startTime != null) {
                val timeDiff = track.endTime!! - track.startTime!!
                result.add(PieModel(
                        track.plan.activityName,
                        TimeUnit.MILLISECONDS.toMinutes(timeDiff).toFloat(),
                        dashboardColors.getNextColor()
                ))
            }
        }
        return result.toList()
    }
}