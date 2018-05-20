package com.mylocations.addlocation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.location.Address
import com.mylocations.MyApplication
import com.mylocations.repository.Repository
import com.mylocations.repository.models.CustomLocation
import com.mylocations.utils.Config

class AddLocationViewModel : ViewModel() {

    var repository: Repository = MyApplication.getRepository()
    // LiveData variables observed in the activity
    val nameError = MutableLiveData<String>() //Name edittext validation error
    val notesError = MutableLiveData<String>() //Notes edittext validation error
    val locationAdded = MutableLiveData<Boolean>()

    //Observable fields for data binding
    val name = ObservableField<String>()
    val notes = ObservableField<String>()
    val addressString = ObservableField<String>("")

    private var address: Address? = null

    init {
        locationAdded.value = false
    }

    fun setAddress(address: Address) {
        this.address = address
        convertAddressToString()
    }

    /**
     * Convert address pbject to String to display the UI and insert in database
     */
    private fun convertAddressToString() {
        val sb = StringBuffer()
        sb.append(if (address!!.getAddressLine(0) != null) address!!.getAddressLine(0) else "")
        sb.append(if (address!!.countryName != null) address!!.countryName else "")
        addressString.set(sb.toString())
    }

    /**
     * Gets the value of locationAdded.
     * This value is observed in the view and is used to find whether the location has been added in database.
     */
    fun isLocationAdded() = locationAdded

    /**
     * Main method called on save button. Performs validation and inserts the location in database.
     * Also notifies the view with the help of locationAdded.
     */
    fun saveLocation() {
        if (name.get() != null) {
            repository.addLocation(CustomLocation(name.get(), if (notes.get() != null) notes.get() else "Notes: ${name.get()}", address!!.latitude, address!!.longitude, addressString.get()))
            locationAdded.value = true
        }else{
            if (name.get() == null) nameError.value = Config.LOCATION_NAME_ERROR
        }
    }

    //Databinding with the layout for name edittext.
    fun setName(name: String){
        this.name.set(name)
    }

    //Databinding with the layout for notes edittext.
    fun setNotes(notes: String){
        this.notes.set(notes)
    }

}
