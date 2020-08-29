package com.laushkina.activityplanning

import com.laushkina.activityplanning.ui.DashboardActivity
import com.laushkina.activityplanning.ui.PlanActivity
import com.laushkina.activityplanning.ui.navigation.Navigation

class NavigationImpl : Navigation {
    override fun getPlansClass(): Class<*> {
        return PlanActivity::class.java
    }

    override fun getDashboardClass(): Class<*> {
        return DashboardActivity::class.java
    }

    override fun getInfoClass(): Class<*> {
        return InfoActivity::class.java
    }
}