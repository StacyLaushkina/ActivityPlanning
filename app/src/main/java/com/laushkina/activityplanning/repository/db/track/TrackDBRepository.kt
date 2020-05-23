package com.laushkina.activityplanning.repository.db.track

import android.content.Context
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.repository.db.ApplicationDatabase
import io.reactivex.Maybe

class TrackDBRepository(context: Context) {
    private val database: ApplicationDatabase = ApplicationDatabase.create(context)

    fun get(): Maybe<List<Track>> {
        return database.trackDao()
            .getTracksAndPlans()
            .map { entities: List<TrackAndPlanDBEntity> -> TrackDBMapper.mapToTrack(entities) }
    }

    fun insert(track: Track) {
        val trackEntity = TrackDBEntity(track)
        database.trackDao().insert(trackEntity)
    }
}