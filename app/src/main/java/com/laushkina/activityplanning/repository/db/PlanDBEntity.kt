package com.laushkina.activityplanning.repository.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.laushkina.activityplanning.model.Plan
import java.util.*

@Entity(tableName = "plans")
class PlanDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val activityName: String,
    val percent: Int?) {

    constructor(plan: Plan) : this(plan.id, plan.name, plan.percent)
}