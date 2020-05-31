package com.laushkina.activityplanning.di.plan

import com.laushkina.activityplanning.ui.plan.PlanView
import dagger.Module
import dagger.Provides

@Module
class PlanViewModule(private val view: PlanView) {
    @Provides
    fun planView(): PlanView {
        return view
    }
}