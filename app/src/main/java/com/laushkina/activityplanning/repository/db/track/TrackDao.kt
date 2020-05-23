package com.laushkina.activityplanning.repository.db.track

import androidx.room.*
import com.laushkina.activityplanning.repository.db.plan.PlanDBEntity
import io.reactivex.Maybe

@Dao
interface TrackDao {
    @Transaction
    @Query("SELECT * FROM tracks")
    fun getTracksAndPlans(): Maybe<List<TrackAndPlanDBEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: TrackDBEntity)

    @Query("SELECT * FROM tracks WHERE planId=:planId")
    fun getByPlanId(planId: Int): TrackDBEntity?
}