package com.laushkina.activityplanning.repository.db.plan

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.laushkina.activityplanning.model.plan.Plan

@Entity(tableName = "plans")
data class PlanDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val activityName: String,
    val percent: Int) {

    constructor(plan: Plan) : this(plan.id, plan.activityName, plan.percent)
}