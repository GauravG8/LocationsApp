package com.mylocations.repository.local

import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.SharedPreferences
import com.mylocations.repository.models.CustomLocation
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Local Repository acts as the medium to communicate to Local database and shared preferences and returns the
 * results to main Repository
 */
class LocalRepository(val appDatabase: AppDatabase?, val preferences: SharedPreferences) {

    //Executor to run operations in background
    private val executor: Executor

    init {
        executor = Executors.newSingleThreadExecutor()
    }

    //Gets all the locations from the database.
    fun getAllLocations() = appDatabase?.localLocationModel()!!.getAllLocations()

    //Gets the location based on id.
    fun getLocation(id: String) = appDatabase?.localLocationModel()!!.getLocation(id)

    //Inserts a new location to the database.
    fun addLocation(customLocation: CustomLocation) {
        executor.execute {
            appDatabase?.localLocationModel()!!.addLocation(customLocation)
        }
    }

    //Inserts a list of locations to the database.
    fun addLocations(list: List<CustomLocation>){
        executor.execute{
            appDatabase?.localLocationModel()!!.addLocations(list)
        }

    }

    //Updates the notes for a particular location based on id.
    fun updateNotes(id: String?, notes: String?){
        executor.execute{
            appDatabase?.localLocationModel()!!.updateNotes(id, notes)
        }
    }

    //Gets the total number of locations in th Location table.
    fun getItemCount() = appDatabase?.localLocationModel()!!.getCount()

    // Function to put the key and value
    fun putData(key: String, value: String){
        preferences.edit().putString(key, value).apply()
    }

    // Function to get the value for a key
    fun getData(key: String) = preferences.getString(key, null)

}