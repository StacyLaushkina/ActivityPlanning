package com.laushkina.activityplanning.repository

import com.laushkina.activityplanning.model.plan.Plan
import io.reactivex.Maybe

interface PlanRepository {
    fun addOrUpdate(plan: Plan)
    fun addOrUpdate(plans: List<Plan>)
    fun get(): Maybe<List<Plan>>
    fun delete(plan: Plan)
}