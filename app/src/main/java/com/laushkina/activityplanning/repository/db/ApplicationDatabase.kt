package com.laushkina.activityplanning.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.laushkina.activityplanning.repository.db.plan.PlanDBEntity
import com.laushkina.activityplanning.repository.db.plan.PlanDao
import com.laushkina.activityplanning.repository.db.track.TrackAndPlanDBEntity
import com.laushkina.activityplanning.repository.db.track.TrackDBEntity
import com.laushkina.activityplanning.repository.db.track.TrackDao

@Database(entities = [PlanDBEntity::class, TrackDBEntity::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase: RoomDatabase() {
    companion object {
        private const val NAME = "rates_database"

        fun create(context: Context): ApplicationDatabase {
            return Room.databaseBuilder(context, ApplicationDatabase::class.java, NAME).build()

        }
    }

    abstract fun planDao(): PlanDao

    abstract fun trackDao(): TrackDao
}