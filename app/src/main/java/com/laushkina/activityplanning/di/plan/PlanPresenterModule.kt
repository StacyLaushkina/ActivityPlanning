package com.laushkina.activityplanning.di.plan

import com.laushkina.activityplanning.di.ServiceModule
import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.ui.plan.PlanPresenter
import com.laushkina.activityplanning.ui.plan.PlanView
import dagger.Module
import dagger.Provides

@Module(includes = [PlanViewModule::class, ServiceModule::class])
class PlanPresenterModule {
    @Provides
    fun getPlanPresenter(view: PlanView, service: PlanService): PlanPresenter {
        return PlanPresenter(view, service)
    }
}