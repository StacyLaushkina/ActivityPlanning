package com.laushkina.activityplanning.ui.plan

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.plan.PlanService
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class PlanPresenter(private val view: PlanView, private val service: PlanService) {
    private val plans: MutableList<Plan> = mutableListOf()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onCreate() {
        compositeDisposable.add(
            service.getPlans().subscribe (
                { plans: List<Plan> -> this.plans.addAll(plans); view.showPlans(plans) },
                { throwable: Throwable -> view.showError(throwable.message) }
            )
        )
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onAddRequested() {
        plans.add(Plan(Random().nextInt(), "", null))

        view.showPlans(plans)
    }

    fun onActivityNameChange(ind: Int, newName: String) {
        synchronized(this) {
            val existingPlan = plans[ind]
            val newPlan = Plan(existingPlan.id, newName, existingPlan.percent)
            finishChange(ind, newPlan)
        }
    }

    fun onPercentChange(ind: Int, newPercent: Int) {
        synchronized(this) {
            val existingPlan = plans[ind]
            val newPlan = Plan(existingPlan.id, existingPlan.activityName, newPercent)
            finishChange(ind, newPlan)
        }
    }

    private fun finishChange(ind: Int, newPlan: Plan) {
        plans.removeAt(ind)
        plans.add(ind, newPlan)

        if (newPlan.isValid()) {
            compositeDisposable.add(service.addOrUpdatePlan(newPlan))
        }
    }
}