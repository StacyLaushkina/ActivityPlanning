package com.laushkina.activityplanning.repository.db

import com.laushkina.activityplanning.model.Plan
import java.util.ArrayList

class PlanDbMapper {
    companion object {
        fun mapToEntity(plans: List<Plan>?): List<PlanDBEntity>? {
            if (plans == null) return null

            val result: MutableList<PlanDBEntity> = ArrayList(plans.size)
            for (plan in plans) {
                result.add(PlanDBEntity(plan))
            }
            return result
        }

        fun mapToRate(entities: List<PlanDBEntity>): List<Plan> {
            val result: MutableList<Plan> = ArrayList(entities.size)
            for (entity in entities) {
                result.add(Plan(entity.id, entity.activityName, entity.percent))
            }
            return result
        }
    }
}