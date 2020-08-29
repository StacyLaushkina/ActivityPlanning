package com.laushkina.activityplanning.repository.db.plan

import android.content.Context
import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.repository.PlanRepository
import com.laushkina.activityplanning.repository.db.ApplicationDatabase
import com.laushkina.activityplanning.repository.db.track.TrackDBEntity
import io.reactivex.Maybe

class PlanDBRepository(context: Context): PlanRepository {
    private val database: ApplicationDatabase = ApplicationDatabase.create(context)

    override fun addOrUpdate(plan: Plan) {
        database.runInTransaction {
            addOrUpdateUnsafe(plan)
        }
    }

    override fun addOrUpdate(plans: List<Plan>) {
        database.runInTransaction {
            plans.map { plan: Plan -> addOrUpdateUnsafe(plan) }
        }
    }

    override fun get(): Maybe<List<Plan>> {
        return database.planDao()
            .loadAll()
            .map { entities: List<PlanDBEntity> -> PlanDBMapper.mapToRate(entities) }
    }

    override fun delete(plan: Plan) {
        database.planDao().delete(PlanDBEntity(plan))
    }

    private fun addOrUpdateUnsafe(plan: Plan) {
        val dao = database.planDao()
        val existingPlan = dao.getById(plan.id)
        val newPlan = PlanDBEntity(plan)

        if (existingPlan == null) {
            dao.insert(newPlan)
        } else {
            dao.update(newPlan)
        }
    }
}