package com.laushkina.activityplanning.repository.db.plan

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans ORDER BY percent DESC")
    fun loadAll(): Maybe<List<PlanDBEntity>>

    @Query("SELECT * FROM plans WHERE id = :id")
    fun getById(id: Int): PlanDBEntity?

    @Update
    fun update(plan: PlanDBEntity)

    @Insert
    fun insert(plan: PlanDBEntity)

    @Query("DELETE FROM plans")
    fun truncate()

    @Delete
    fun delete(plan: PlanDBEntity)
}