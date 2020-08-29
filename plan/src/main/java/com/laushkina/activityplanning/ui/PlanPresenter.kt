package com.laushkina.activityplanning.ui

import androidx.annotation.VisibleForTesting
import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.plan.PlanService
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class PlanPresenter(private val view: PlanView, private val service: PlanService) {
    private val plans: MutableList<Plan> = mutableListOf()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var hoursPerDay = PlanService.DEFAULT_HOURS_PER_DAY

    fun onCreate() {
        compositeDisposable.add(
            service.getPlans().subscribe(
                { plans: List<Plan> ->
                    if (plans.isEmpty()) {
                        view.showInitWithSampleValuesButton()
                    } else {
                        val hoursPerDay = service.getHoursPerDay(plans)
                        this.plans.addAll(plans)
                        this.hoursPerDay = hoursPerDay
                        initPlans(this.plans, hoursPerDay)
                    }
                    view.showHourPerDaySpinner(
                        PlanService.HOURS_PER_DAY_VARIANTS,
                        PlanService.HOURS_PER_DAY_VARIANTS.indexOf(this.hoursPerDay)
                    )
                },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onAddRequested() {
        val percent = getRemainingPercent()
        if (percent == 0) {
            view.showError("Cannot add new activity. Time is filled.")
        } else {
            view.showAddPlanDialog(percent)
        }
    }

    fun onRemoveRequested(ind: Int) {
        synchronized(this) {
            val planToRemove = plans[ind]
            plans.removeAt(ind)
            compositeDisposable.add(service.remove(planToRemove))
            updatePlans(plans, hoursPerDay)
        }
    }

    fun onPlanConfirmed(name: String, percent: Int) {
        synchronized(this) {
            val plan = Plan(Random().nextInt(), name, percent, hoursPerDay)
            plans.add(plan)
            compositeDisposable.add(service.addOrUpdatePlan(plan))
            if (plans.size == 1) {
                initPlans(plans, hoursPerDay)
            } else {
                updatePlans(plans, hoursPerDay)
            }
        }
    }

    fun onHoursPerDayChanged(hours: Int?) {
        if (hours == null || hours == hoursPerDay) {
            return
        }
        hoursPerDay = hours
        if (plans.isEmpty()) {
            return
        }
        compositeDisposable.add(
            service.updateHoursPerDay(plans, hoursPerDay).subscribe(
                { plans: List<Plan> ->
                    this.plans.clear()
                    this.plans.addAll(plans)
                    updatePlans(this.plans, hoursPerDay)
                },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
    }

    fun onFillWithSampleRequested() {
        synchronized(this) {
            val hardWork = Plan(Random().nextInt(), "Hard work", 50, hoursPerDay)
            compositeDisposable.add(service.addOrUpdatePlan(hardWork))
            val docs = Plan(Random().nextInt(), "Documentation", 20, hoursPerDay)
            compositeDisposable.add(service.addOrUpdatePlan(docs))
            val meetings = Plan(Random().nextInt(), "Meetings", 20, hoursPerDay)
            compositeDisposable.add(service.addOrUpdatePlan(meetings))
            val idle = Plan(Random().nextInt(), "Idle", 10, hoursPerDay)
            compositeDisposable.add(service.addOrUpdatePlan(idle))
            plans.addAll(listOf(hardWork, docs, meetings, idle))
        }

        view.hideInitWithSampleValuesButton()
        initPlans(plans, hoursPerDay)
    }

    @VisibleForTesting
    fun getRemainingPercent(): Int {
        var percentSum = 0
        for (plan in plans) {
            percentSum += plan.percent
        }
        return 100 - percentSum
    }

    private fun initPlans(plans: List<Plan>, hoursPerDay: Int) {
        view.initPlans(plans, hoursPerDay)
        updateSamplesButton(plans)
    }

    private fun updatePlans(plans: List<Plan>, hoursPerDay: Int) {
        view.updatePlans(plans, hoursPerDay)
        updateSamplesButton(plans)
    }

    private fun updateSamplesButton(plans: List<Plan>) {
        if (plans.isEmpty()) {
            view.showInitWithSampleValuesButton()
        } else {
            view.hideInitWithSampleValuesButton()
        }
    }
}