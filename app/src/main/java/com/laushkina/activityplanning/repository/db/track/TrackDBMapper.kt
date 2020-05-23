package com.laushkina.activityplanning.repository.db.track

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.track.Track
import java.util.ArrayList

class TrackDBMapper {
    companion object {
        fun mapToEntity(plans: List<Track>?): List<TrackAndPlanDBEntity>? {
            if (plans == null) return null

            val result: MutableList<TrackAndPlanDBEntity> = ArrayList(plans.size)
            for (plan in plans) {
                result.add(
                    TrackAndPlanDBEntity(
                        plan
                    )
                )
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
                        entity.track.endTime
                    )
                )
            }
            return result
        }
    }
}