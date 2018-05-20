package com.mylocations.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.mylocations.repository.models.CustomLocation

/**
 * Singleton Database - Room architecture component.
 * Holds a reference to the Entity DAOs.
 */
@Database(entities = arrayOf(CustomLocation::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    //LocatioDao object
    abstract fun localLocationModel(): LocationDao

    companion object {

        private var appDatabase: AppDatabase? = null

        //Function to get the singleton instance of AppDatabase
        fun getInstance(context: Context): AppDatabase? {
            if (appDatabase == null) {
                synchronized(AppDatabase::class) {
                    appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "location_db").build()
                }
            }

            return appDatabase
        }
    }
}