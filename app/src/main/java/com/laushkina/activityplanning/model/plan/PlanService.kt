package com.laushkina.activityplanning.model.plan

import com.laushkina.activityplanning.repository.db.plan.PlanDBRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlanService(private val dbRepository: PlanDBRepository) {

    fun getPlans(): Maybe<List<Plan>> {
        return dbRepository.get()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addOrUpdatePlan(plan: Plan): Disposable {
        return Maybe.just(plan)
            .map { tmp: Plan -> dbRepository.addOrUpdate(tmp) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}