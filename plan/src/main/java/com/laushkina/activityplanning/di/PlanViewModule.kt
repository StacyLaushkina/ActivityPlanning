package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.ui.PlanView
import dagger.Module
import dagger.Provides

@Module
class PlanViewModule(private val view: PlanView) {
    @Provides
    fun planView(): PlanView {
        return view
    }
}