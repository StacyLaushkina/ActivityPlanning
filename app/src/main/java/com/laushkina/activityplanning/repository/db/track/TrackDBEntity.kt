package com.laushkina.activityplanning.repository.db.track

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.laushkina.activityplanning.model.track.Track

@Entity(tableName = "tracks")
data class TrackDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val planId: Int,
    val startTime: Long?,
    val endTime: Long?) {

    constructor(track: Track) : this(track.id, track.plan.id, track.startTime, track.endTime)
}