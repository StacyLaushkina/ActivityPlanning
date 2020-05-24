package com.laushkina.activityplanning.ui.track

import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import io.reactivex.disposables.CompositeDisposable

class TracksPresenter(private val view: TracksView, private val service: TrackService) {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onCreate() {
        compositeDisposable.add(service.getTracks().subscribe (
            { tracks: List<Track> -> view.showTracks(tracks) },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onTrackStart(ind: Int, track: Track) {
        track.startTime = System.currentTimeMillis()
        updateTrack(track)
    }

    fun onTrackContinue(ind: Int, track: Track) {
        track.endTime = null
        updateTrack(track)
    }

    fun onTrackFinish(ind: Int, track: Track) {
        track.endTime = System.currentTimeMillis()
        updateTrack(track)
    }

    private fun updateTrack(track: Track) {
        compositeDisposable.add(service.updateTrack(track).subscribe (
            { tracks: List<Track> -> view.showTracks(tracks) },
            { throwable: Throwable -> view.showError(throwable.message) }
        ))
    }
}