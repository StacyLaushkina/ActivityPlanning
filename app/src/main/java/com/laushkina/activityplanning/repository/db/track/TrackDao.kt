package com.laushkina.activityplanning.repository.db.track

import androidx.room.*
import com.laushkina.activityplanning.repository.db.plan.PlanDBEntity
import io.reactivex.Maybe

@Dao
interface TrackDao {
    @Transaction
    @Query("SELECT * FROM tracks WHERE date LIKE :date")
    fun getTracksAndPlans(date: String): Maybe<List<TrackAndPlanDBEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: TrackDBEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(tracks: List<TrackDBEntity>)
}