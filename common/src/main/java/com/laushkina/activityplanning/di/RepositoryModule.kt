package com.laushkina.activityplanning.di

import android.content.Context
import com.laushkina.activityplanning.repository.PlanRepository
import com.laushkina.activityplanning.repository.TrackRepository
import com.laushkina.activityplanning.repository.db.plan.PlanDBRepository
import com.laushkina.activityplanning.repository.db.track.TrackDBRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun getPlanRepository(context: Context): PlanRepository {
        return PlanDBRepository(context)
    }

    @Provides
    fun getTrackRepository(context: Context): TrackRepository {
        return TrackDBRepository(context)
    }
}