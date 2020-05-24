package com.laushkina.activityplanning.repository.db.track

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.track.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackDBMapper {
    companion object {
        private const val DATE_FORMAT = "dd MMM yyyy"

        fun mapToEntity(plans: List<Track>?): List<TrackDBEntity>? {
            if (plans == null) return null

            val result: MutableList<TrackDBEntity> = ArrayList(plans.size)
            for (plan in plans) {
                result.add(TrackDBEntity(plan))
            }
            return result
        }

        fun mapToTrack(entities: List<TrackAndPlanDBEntity>): List<Track> {
            val result: MutableList<Track> = ArrayList(entities.size)
            for (entity in entities) {
                result.add(
                    Track(
                        entity.track.id,
                        Plan(
                            entity.plan.id,
                            entity.plan.activityName,
                            entity.plan.percent
                        ),
                        entity.track.startTime,
                        entity.track.endTime,
                        parseDateTime(entity.track.date)
                    )
                )
            }
            return result
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