package com.mylocations.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import android.location.Geocoder
import com.mylocations.repository.local.AppDatabase
import com.mylocations.repository.local.LocalRepository
import com.mylocations.repository.models.CustomLocation
import com.mylocations.repository.remote.OnRemoteDataReadyCallback
import com.mylocations.repository.remote.RemoteRepository
import com.mylocations.repository.remote.ResponseModel
import com.mylocations.utils.Config
import org.json.JSONObject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Repository(val localRepository: LocalRepository?, val remoteRepository: RemoteRepository?, val geoCoder: Geocoder? = null) {

    /**
     * Inserts the location in the local database
      */
    fun addLocation(customLocation: CustomLocation) {
        localRepository?.addLocation(customLocation)
    }

    /**
     * Gets all the locations from the database. This value is observed.
     */
    fun getAllLocations() = localRepository?.getAllLocations()

    /**
     * Gets location based on id from the database. This value is observed.
     */
    fun getLocation(id: String) = localRepository?.getLocation(id);

    /**
     * Update notes for the location in the local repository.
     */
    fun updateNotes(id: String?, notes: String?){
        localRepository?.updateNotes(id, notes)
    }

    /**
     * Called on first app launch to import default locations from JSON files and insert into database.
     * Once done, notifies the called through callback interface method. Remote repository is used for easier management in future changes.
     */
    fun getLocationsFromFile(onDataReadyCallback: OnDataReadyCallback){
        remoteRepository?.getLocationsFromFile(object : OnRemoteDataReadyCallback{
            override fun onRemoteDataReady(responseModel: ResponseModel) {
                val list = ArrayList<CustomLocation>()
                for (item in responseModel.locations){
                    val address = Config.getAddressFromLatLng(geoCoder, item.lat, item.lng)
                    list.add(CustomLocation(item.name, "Notes: ${item.name}", item.lat, item.lng, address))
                }
                localRepository?.addLocations(list)
                putPreference(Config.FIRST_TIME, Config.LAUNCH_DATA_RETRIEVED)
                onDataReadyCallback.sendStatus(true)
            }
        })
    }

    // Method the get the value from shared preference
    fun getPreference(key: String) = localRepository?.getData(key)

    // Method to put the value to shared preference
    fun putPreference(key: String, value: String) = localRepository?.putData(key, value)

    //Gets the total number of locations. This value is observed.
    fun getItemCount() = localRepository?.getItemCount()


}

/**
 * Callback interface to notify the calling class after inserting the locations in database
 */
interface OnDataReadyCallback{
    fun sendStatus(status: Boolean)
}
