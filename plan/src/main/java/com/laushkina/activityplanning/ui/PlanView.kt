package com.laushkina.activityplanning.ui

import com.laushkina.activityplanning.model.plan.Plan

interface PlanView {
    fun initPlans(plans: List<Plan>, hoursPerDay: Int)
    fun updatePlans(plans: List<Plan>, hoursPerDay: Int)
    fun showError(message: String?)
    fun showHourPerDaySpinner(variants: Array<Int>, selectedItem: Int)
    fun showAddPlanDialog(remainingPercent: Int)
    fun showInitWithSampleValuesButton()
    fun hideInitWithSampleValuesButton()
}