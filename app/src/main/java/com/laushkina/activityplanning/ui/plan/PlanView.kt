package com.laushkina.activityplanning.ui.plan

import com.laushkina.activityplanning.model.Plan

interface PlanView {
    fun showPlans(plans: List<Plan>)
    fun updatePlans(plans: MutableList<Plan>)
    fun showError(message: String?)
}