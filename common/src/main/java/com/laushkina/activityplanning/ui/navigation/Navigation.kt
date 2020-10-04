package com.laushkina.activityplanning.ui.navigation

interface Navigation {
    fun getPlansClass(): Class<*>
    fun getDashboardClass(): Class<*>
    fun getTrackClass(): Class<*>
    fun getInfoClass(): Class<*>
}