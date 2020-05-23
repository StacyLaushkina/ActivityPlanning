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
        // It's better to do it in service. But transaction is needed here
        database.transactionExecutor.execute {
            database.planDao().insert(PlanDBEntity(plan))
            val existingTrack = database.trackDao().getByPlanId(plan.id)
            if (existingTrack == null) {
                database.trackDao().insert(TrackDBEntity(Random().nextInt(), plan.id, null, null))
            }
        }
    }

    fun get(): Maybe<List<Plan>> {
        return database.planDao()
            .loadAll()
            .map { entities: List<PlanDBEntity> -> PlanDBMapper.mapToRate(entities) }
    }
}