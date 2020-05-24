package com.laushkina.activityplanning.ui.plan

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.plan.PlanService
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class PlanPresenter(private val view: PlanView, private val service: PlanService) {
    private val plans: MutableList<Plan> = mutableListOf()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var hoursPerDay = 8
    private val hoursPerDayVariants = arrayOf(7, 8, 9, 10, 11, 12)

    fun onCreate() {
        compositeDisposable.add(
            service.getPlans().subscribe (
                { plans: List<Plan> -> this.plans.addAll(plans); view.showPlans(plans, hoursPerDay) },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
        view.showHourPerDaySpinner(hoursPerDayVariants, 1)
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onAddRequested() {
        view.showAddActivityDialog(getRemainingPercent())
        view.showPlans(plans, hoursPerDay)
    }

    fun onPlanConfirmed(name: String, percent: Int) {
        synchronized(this) {
            val plan = Plan(Random().nextInt(), name, percent)
            plans.add(plan)
            compositeDisposable.add(service.addOrUpdatePlan(plan))
            view.updatePlans(plans, hoursPerDay)
        }
    }

    fun onActivityRemoveRequested(ind: Int) {
        synchronized(this) {
            val planToRemove = plans[ind]
            plans.removeAt(ind)
            compositeDisposable.add(service.remove(planToRemove))
            view.updatePlans(plans, hoursPerDay)
        }
    }

    fun onHoursPerDayChanged(variant: Int?) {
        if (variant == null) {
            return
        }
        hoursPerDay = variant
        view.updatePlans(plans, hoursPerDay)
    }

    private fun getRemainingPercent(): Int {
        var percentSum = 0
        for (plan in plans) {
            percentSum += plan.percent
        }
        return 100 - percentSum
    }
}