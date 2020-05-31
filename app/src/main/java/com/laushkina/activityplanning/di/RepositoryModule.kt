package com.laushkina.activityplanning.di

import android.content.Context
import com.laushkina.activityplanning.repository.db.plan.PlanDBRepository
import com.laushkina.activityplanning.repository.db.track.TrackDBRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun getPlanRepository(context: Context): PlanDBRepository {
        return PlanDBRepository(context)
    }

    @Provides
    fun getTrackRepository(context: Context): TrackDBRepository {
        return TrackDBRepository(context)
    }
}