package com.laushkina.activityplanning.repository.db.plan

import com.laushkina.activityplanning.model.plan.Plan

class PlanDBMapper {
    companion object {
        fun mapToEntity(plans: List<Plan>?): List<PlanDBEntity>? {
            if (plans == null) return null
            return plans.map { plan: Plan ->
                PlanDBEntity(plan)
            }
        }

        fun mapToRate(entities: List<PlanDBEntity>): List<Plan> {
            return entities.map { entity: PlanDBEntity ->
                Plan(entity.id, entity.name, entity.percent, entity.hoursPerDay)
            }
        }
    }
}