package com.laushkina.activityplanning.model.plan

import com.laushkina.activityplanning.repository.PlanRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlanService(private val repository: PlanRepository) {
    companion object {
        const val DEFAULT_HOURS_PER_DAY = 8
        val HOURS_PER_DAY_VARIANTS = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
    }

    fun getPlans(): Maybe<List<Plan>> {
        return repository.get()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getHoursPerDay(plans: List<Plan>): Int {
        val planWithMaxHours = plans.maxBy { plan: Plan -> plan.hoursPerDay }
        return planWithMaxHours?.hoursPerDay ?: DEFAULT_HOURS_PER_DAY
    }

    fun addOrUpdatePlan(plan: Plan): Disposable {
        return Maybe.just(plan)
            .map { tmp: Plan -> repository.addOrUpdate(tmp) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun updateHoursPerDay(plans: List<Plan>, hours: Int): Maybe<List<Plan>> {
        plans.forEach { plan: Plan ->
            plan.hoursPerDay = hours
        }
        return Maybe.just(plans)
            .map { tmp: List<Plan> -> repository.addOrUpdate(tmp) }
            .flatMap { repository.get() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun remove(plan: Plan): Disposable {
        return Maybe.just(plan)
            .map { tmp: Plan -> repository.delete(tmp) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}