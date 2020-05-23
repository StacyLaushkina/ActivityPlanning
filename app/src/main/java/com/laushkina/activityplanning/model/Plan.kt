package com.laushkina.activityplanning.model

data class Plan(val id: Int, val name: String, val percent: Int?) {

    fun isValid(): Boolean {
        return name.isNotEmpty() && percent != null
    }
}