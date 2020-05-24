package com.laushkina.activityplanning.ui.plan

import com.laushkina.activityplanning.model.plan.Plan

interface PlanView {
    fun showPlans(plans: List<Plan>, hoursPerDay: Int)
    fun updatePlans(plans: MutableList<Plan>, hoursPerDay: Int)
    fun showError(message: String?)
    fun showHourPerDaySpinner(variants: Array<Int>, selectedItem: Int)
    fun showAddActivityDialog(remainingPercent: Int)
}