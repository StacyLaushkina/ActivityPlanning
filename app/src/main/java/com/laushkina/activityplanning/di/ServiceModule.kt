package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.repository.PlanRepository
import com.laushkina.activityplanning.repository.TrackRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class, RepositoryModule::class])
class ServiceModule {
    @Provides
    fun getPlanService(planDataSource: PlanRepository): PlanService {
        return PlanService(planDataSource)
    }

    @Provides
    fun getTrackService(trackDataSource: TrackRepository,
                        planDataSource: PlanRepository): TrackService {
        return TrackService(trackDataSource, planDataSource)
    }
}