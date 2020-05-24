package com.laushkina.activityplanning.repository.db.track

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.repository.db.plan.PlanDBEntity

@Entity(
    tableName = "tracks", foreignKeys = [ForeignKey(
        entity = PlanDBEntity::class,
        parentColumns = ["id"],
        childColumns = ["planId"],
        onDelete = CASCADE
    )]
)
data class TrackDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val planId: Int,
    val startTime: Long?,
    val endTime: Long?,
    val date: String) {

    constructor(track: Track) : this(
        track.id,
        track.plan.id,
        track.startTime,
        track.endTime,
        TrackDBMapper.dateToString(track.date)
    )
}