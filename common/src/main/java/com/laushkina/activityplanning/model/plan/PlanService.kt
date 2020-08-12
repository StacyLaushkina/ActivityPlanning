package com.laushkina.activityplanning.model.plan

import com.laushkina.activityplanning.repository.PlanRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlanService(private val repository: PlanRepository) {

    fun getPlans(): Maybe<List<Plan>> {
        return repository.get()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addOrUpdatePlan(plan: Plan): Disposable {
        return Maybe.just(plan)
            .map { tmp: Plan -> repository.addOrUpdate(tmp) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun updateHoursPerDay(plans: List<Plan>, hours: Int): Disposable {
        plans.forEach { plan: Plan ->
            plan.hoursPerDay = hours
        }
        return Maybe.just(plans)
            .map { tmp: List<Plan> -> repository.addOrUpdate(tmp) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun remove(plan: Plan): Disposable{
        return Maybe.just(plan)
            .map { tmp: Plan -> repository.delete(tmp) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}