package com.laushkina.activityplanning.repository.db.track

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.track.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackDBMapper {
    companion object {
        private const val DATE_FORMAT = "dd MMM yyyy"

        fun mapToEntity(tracks: List<Track>?): List<TrackDBEntity>? {
            if (tracks == null) return null
            return tracks.map { track: Track ->
                TrackDBEntity(track)
            }
        }

        fun mapToTrack(entities: List<TrackAndPlanDBEntity>): List<Track> {
            return entities.map { entity: TrackAndPlanDBEntity ->
                Track(
                    entity.track.id,
                    Plan(
                        entity.plan.id,
                        entity.plan.name,
                        entity.plan.percent,
                        entity.plan.hoursPerDay
                    ),
                    entity.track.startTime,
                    entity.track.duration,
                    entity.track.isInProgress,
                    entity.track.isFinished,
                    parseDateTime(entity.track.date)
                )
            }
        }

        fun dateToString(date: Date): String {
            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
            return formatter.format(date)
        }

        private fun parseDateTime(dateTime: String): Date {
            val parser = SimpleDateFormat(DATE_FORMAT, Locale.US)
            return parser.parse(dateTime)
        }
    }
}