package com.laushkina.activityplanning.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlanDBEntity::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase: RoomDatabase() {
    companion object {
        private const val NAME = "rates_database"

        fun create(context: Context): ApplicationDatabase {
            return Room.databaseBuilder(context, ApplicationDatabase::class.java, NAME).build()

        }
    }

    abstract fun planDao(): PlanDao
}