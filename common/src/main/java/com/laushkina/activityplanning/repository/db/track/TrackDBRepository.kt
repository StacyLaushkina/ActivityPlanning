package com.laushkina.activityplanning.repository.db.track

import android.content.Context
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.repository.TrackRepository
import com.laushkina.activityplanning.repository.db.ApplicationDatabase
import io.reactivex.Maybe
import java.util.*

class TrackDBRepository(context: Context): TrackRepository {
    private val database: ApplicationDatabase = ApplicationDatabase.create(context)

    override fun get(date: Date): Maybe<List<Track>> {
        return database.trackDao()
            .getTracksAndPlans(TrackDBMapper.dateToString(date))
            .map { entities: List<TrackAndPlanDBEntity> -> TrackDBMapper.mapToTrack(entities) }
    }

    override fun insert(track: Track) {
        val trackEntity = TrackDBEntity(track)
        database.trackDao().insert(trackEntity)
    }

    override fun insertAll(tracks: List<Track>) {
        val trackEntities = TrackDBMapper.mapToEntity(tracks)
        database.trackDao().insertAll(trackEntities!!)
    }
}