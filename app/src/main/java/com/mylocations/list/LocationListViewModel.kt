package com.mylocations.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.mylocations.MyApplication
import com.mylocations.list.adapters.LocationListItem
import com.mylocations.repository.Repository
import com.mylocations.repository.models.CustomLocation
import com.mylocations.utils.Config

/**
 * View model class for Location List
 */
class LocationListViewModel() : ViewModel() {

    //Communicates with the local and remote repository
    var repository : Repository = MyApplication.getRepositoryInstance()

    // LiveData location list observed in the activity
    val locationList = MutableLiveData<List<LocationListItem>>()

    // Current location
    lateinit var location: Location

    /**
     * Gets the location list from the database. This value is observed in the view and passed on to the adapter
     */
    fun loadLocations() = repository.getAllLocations()

    /**
     * Create a list of LocationListItem from a list of CustomLocation.
     * The distance from current location is calculted for every location and is sorted in increasing order.
     */
    fun createLocationList(list: List<CustomLocation>){
        val newList = ArrayList<LocationListItem>()
        for (item in list){
            val results = FloatArray(1)
            Location.distanceBetween(location.latitude, location.longitude, item.latitude, item.longitude, results)
            newList.add(LocationListItem(item, results[0], Config.getDistanceString(results[0])))
        }
        locationList.value = newList.sortedWith(compareBy(LocationListItem::distance))
    }
}