package com.laushkina.activityplanning.di.plan

import com.laushkina.activityplanning.ui.plan.PlanPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlanPresenterModule::class])
interface PlanPresenterComponent {
    fun getPlanPresenter(): PlanPresenter
}