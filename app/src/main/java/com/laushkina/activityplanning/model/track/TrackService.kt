package com.laushkina.activityplanning.model.track

import com.laushkina.activityplanning.repository.db.track.TrackDBRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrackService(private val dbRepository: TrackDBRepository) {

    fun getTracks(): Maybe<List<Track>> {
        return dbRepository.get()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateTrack(track: Track): Maybe<List<Track>> {
        return Maybe.just(track)
            .map { dbRepository.insert(track) }
            .flatMap { dbRepository.get() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}