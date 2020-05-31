package com.laushkina.activityplanning.repository

import com.laushkina.activityplanning.model.track.Track
import io.reactivex.Maybe
import java.util.*

interface TrackRepository {
    fun get(date: Date): Maybe<List<Track>>
    fun insert(track: Track)
    fun insertAll(tracks: List<Track>)
}