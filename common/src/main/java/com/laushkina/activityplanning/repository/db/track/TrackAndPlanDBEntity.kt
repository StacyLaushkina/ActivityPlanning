package com.laushkina.activityplanning.repository.db.track

import androidx.room.Embedded
import androidx.room.Relation
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.repository.db.plan.PlanDBEntity

data class TrackAndPlanDBEntity(
    @Embedded val track: TrackDBEntity,
    @Relation(
        parentColumn = "planId",
        entityColumn = "id"
    )
    val plan: PlanDBEntity
)