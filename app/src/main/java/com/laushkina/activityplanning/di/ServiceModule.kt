package com.laushkina.activityplanning.di

import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.repository.db.plan.PlanDBRepository
import com.laushkina.activityplanning.repository.db.track.TrackDBRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class, RepositoryModule::class])
class ServiceModule {
    @Provides
    fun getPlanService(planDataSource: PlanDBRepository): PlanService {
        return PlanService(planDataSource)
    }

    @Provides
    fun getTrackService(trackDataSource: TrackDBRepository,
                        planDataSource: PlanDBRepository): TrackService {
        return TrackService(trackDataSource, planDataSource)
    }
}