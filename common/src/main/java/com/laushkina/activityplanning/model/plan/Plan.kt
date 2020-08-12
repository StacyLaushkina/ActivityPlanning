package com.laushkina.activityplanning.model.plan

data class Plan(val id: Int, val activityName: String, val percent: Int, var hoursPerDay: Int)