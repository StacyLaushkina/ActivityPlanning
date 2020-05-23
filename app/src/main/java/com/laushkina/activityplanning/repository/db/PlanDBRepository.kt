package com.laushkina.activityplanning.repository.db

import android.content.Context
import com.laushkina.activityplanning.model.Plan
import io.reactivex.Maybe

class PlanDBRepository(context: Context) {
    private val database: ApplicationDatabase = ApplicationDatabase.create(context)

    fun save(plans: List<Plan>?) {
        database.transactionExecutor.execute {
            database.planDao().truncate()
            database.planDao().save(PlanDbMapper.mapToEntity(plans))
        }
    }

    fun addOrUpdate(plan: Plan) {
        database.planDao().insert(PlanDBEntity(plan))
    }

    fun get(): Maybe<List<Plan>> {
        return database.planDao()
            .loadAll()
            .map { entities: List<PlanDBEntity> -> PlanDbMapper.mapToRate(entities) }
    }
}