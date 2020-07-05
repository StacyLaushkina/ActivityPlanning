package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.ui.PlanPresenter
import com.laushkina.activityplanning.ui.PlanView
import dagger.Module
import dagger.Provides

@Module(includes = [PlanViewModule::class, ServiceModule::class])
class PlanPresenterModule {
    @Provides
    fun getPlanPresenter(view: PlanView, service: PlanService): PlanPresenter {
        return PlanPresenter(view, service)
    }
}