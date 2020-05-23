package com.laushkina.activityplanning.model.plan

data class Plan(val id: Int, val activityName: String, val percent: Int?) {

    fun isValid(): Boolean {
        return activityName.isNotEmpty() && percent != null
    }
}