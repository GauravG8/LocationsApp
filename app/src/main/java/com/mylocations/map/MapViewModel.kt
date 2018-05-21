package com.mylocations.map

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.location.Address
import com.mylocations.MyApplication
import com.mylocations.repository.OnDataReadyCallback
import com.mylocations.repository.Repository
import com.mylocations.repository.models.CustomLocation
import com.mylocations.utils.Config
import java.util.*

/**
 * View model class for Map View
 */
class MapViewModel : ViewModel() {
    //Communicates with the local and remote repository
    var repository : Repository = MyApplication.getRepositoryInstance()

    // LiveData variables observed in the activity
    val isLoading = MutableLiveData<Boolean>()
    val listItemLive = MutableLiveData<Boolean>()
    val selectLocationLive = MutableLiveData<Boolean>()
    val isSelectingLive = MutableLiveData<Boolean>()

    //Observable fields for data binding
    val isSelecting = ObservableField<Boolean>(false)
    val markedText = ObservableField<String>()
    val itemCount = ObservableField<String>()
    val address = ObservableField<Address>()

    init {
        isLoading.value = false
        isSelectingLive.value = false
        listItemLive.value = false;
        selectLocationLive.value = false;
    }

    /**
     * Gets the list of locations from the database. This value is observed in the view.
     */
    fun getLocationList(): LiveData<List<CustomLocation>>? =  repository.getAllLocations()

    /**
     * Called at the app launch. If this is the first time launch, default locations are imported
     * from the json file stored in assets and inserted to the database. Else, do nothing.
     */
    fun appStart(){
        val firstTime = repository.getPreference(Config.FIRST_TIME);
        if (firstTime == null || firstTime.equals(Config.LAUNCH_DATA_NOT_RETRIEVED)){
            isLoading.value = true
            repository.getLocationsFromFile(object: OnDataReadyCallback{
                override fun sendStatus(status: Boolean) {
                    isLoading.postValue(false)
                }
            })
        }
    }

    /**
     * Gets the number of locations from the database. This value is observed in the view.
     */
    fun getItemCount(): LiveData<String>? = repository.getItemCount()

    /**
     * Click function for displaying the list of locations.
     */
    fun listItems(){
        listItemLive.value = true
    }

    /**
     * Click function for add the location.
     */
    fun selectLocation(){
        markedText.set("")
        isSelectingLive.value = false
        isSelecting.set(false)
        selectLocationLive.value = true
    }

    /**
     * Click function for the floating action button.
     * Switches the visibility of Add location layout and List items layout.
     */
    fun fabClicked(){
        markedText.set("")
        isSelectingLive.value = isSelecting.get()?.not()
        isSelecting.set(isSelecting.get()?.not())
    }
}