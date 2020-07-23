package com.laushkina.activityplanning.model.track

import com.laushkina.activityplanning.model.plan.Plan
import java.util.*

data class Track(
    val id: Int,
    val plan: Plan,
    var startTime: Long?,
    var duration: Long,
    var isInProgress: Boolean,
    var isFinished: Boolean,
    val date: Date
)