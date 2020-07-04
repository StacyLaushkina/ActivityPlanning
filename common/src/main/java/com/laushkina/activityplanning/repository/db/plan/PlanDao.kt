package com.laushkina.activityplanning.repository.db.plan

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans ORDER BY percent DESC")
    fun loadAll(): Maybe<List<PlanDBEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(plan: PlanDBEntity)

    @Insert
    fun save(plans: List<PlanDBEntity>?)

    @Query("DELETE FROM plans")
    fun truncate()

    @Delete
    fun delete(plan: PlanDBEntity)
}