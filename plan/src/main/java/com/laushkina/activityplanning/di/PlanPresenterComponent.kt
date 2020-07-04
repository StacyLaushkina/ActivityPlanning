package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.ui.PlanPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlanPresenterModule::class])
interface PlanPresenterComponent {
    fun getPlanPresenter(): PlanPresenter
}