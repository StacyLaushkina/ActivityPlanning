package com.laushkina.activityplanning.repository.db.plan

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.laushkina.activityplanning.model.plan.Plan

@Entity(tableName = "plans")
data class PlanDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val percent: Int,
    val hoursPerDay: Int) {

    constructor(plan: Plan) : this(plan.id, plan.name, plan.percent, plan.hoursPerDay)
}