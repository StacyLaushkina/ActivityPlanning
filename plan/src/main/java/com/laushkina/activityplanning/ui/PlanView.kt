package com.laushkina.activityplanning.ui

import com.laushkina.activityplanning.model.plan.Plan

interface PlanView {
    fun showPlans(plans: List<Plan>, hoursPerDay: Int)
    fun updatePlans(plans: MutableList<Plan>, hoursPerDay: Int)
    fun showError(message: String?)
    fun showHourPerDaySpinner(variants: Array<Int>, selectedItem: Int)
    fun showAddActivityDialog(remainingPercent: Int)
    fun showInitWithSampleValuesButton()
    fun hideInitWithSampleValuesButton()
}