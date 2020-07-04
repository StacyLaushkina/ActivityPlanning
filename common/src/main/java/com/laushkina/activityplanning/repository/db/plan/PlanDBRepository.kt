package com.laushkina.activityplanning.repository.db.plan

import android.content.Context
import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.repository.PlanRepository
import com.laushkina.activityplanning.repository.db.ApplicationDatabase
import io.reactivex.Maybe

class PlanDBRepository(context: Context): PlanRepository {
    private val database: ApplicationDatabase = ApplicationDatabase.create(context)

    fun save(plans: List<Plan>?) {
        database.transactionExecutor.execute {
            database.planDao().truncate()
            database.planDao().save(PlanDBMapper.mapToEntity(plans))
        }
    }

    override fun addOrUpdate(plan: Plan) {
        database.planDao().insert(PlanDBEntity(plan))
    }

    override fun get(): Maybe<List<Plan>> {
        return database.planDao()
            .loadAll()
            .map { entities: List<PlanDBEntity> -> PlanDBMapper.mapToRate(entities) }
    }

    override fun delete(plan: Plan) {
        database.planDao().delete(PlanDBEntity(plan))
    }
}