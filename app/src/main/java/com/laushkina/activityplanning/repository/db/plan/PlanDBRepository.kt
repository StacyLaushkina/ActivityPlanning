package com.laushkina.activityplanning.repository.db.plan

import android.content.Context
import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.repository.db.ApplicationDatabase
import com.laushkina.activityplanning.repository.db.track.TrackDBEntity
import io.reactivex.Maybe
import java.util.*

class PlanDBRepository(context: Context) {
    private val database: ApplicationDatabase = ApplicationDatabase.create(context)

    fun save(plans: List<Plan>?) {
        database.transactionExecutor.execute {
            database.planDao().truncate()
            database.planDao().save(PlanDBMapper.mapToEntity(plans))
        }
    }

    fun addOrUpdate(plan: Plan) {
        database.planDao().insert(PlanDBEntity(plan))
    }

    fun get(): Maybe<List<Plan>> {
        return database.planDao()
            .loadAll()
            .map { entities: List<PlanDBEntity> -> PlanDBMapper.mapToRate(entities) }
    }

    fun delete(plan: Plan) {
        database.planDao().delete(PlanDBEntity(plan))
    }
}