package com.mylocations.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.mylocations.MyApplication
import com.mylocations.repository.Repository
import com.mylocations.repository.models.CustomLocation
import com.mylocations.utils.Config

class LocationDetailViewModel : ViewModel() {

    var repository: Repository = MyApplication.getRepository()

    //Observable fields for data binding
    val name = ObservableField<String>()
    val notes = ObservableField<String>()
    val latitude = ObservableField<Double>()
    val longitude = ObservableField<Double>()
    val address = ObservableField<String>()
    val editing = ObservableField<Boolean>(false)

    // LiveData variables observed in the activity
    val notesUpdated = MutableLiveData<String>()

    var id: String? = null

    /**
     * Gets the location from the database. This value is observed in the view
     */
    fun getLocation(id: String): LiveData<CustomLocation>? =  repository.getLocation(id);

    /**
     * Called when the user edits the notes of the location.
     * Updates the database and notifies the view.
     */
    fun updateNotes(){
        repository.updateNotes(id, notes.get())
        editing.set(false)
        notesUpdated.value = Config.NOTES_UPDATED
    }

    // Databinding for notes edittext
    fun setNotes(notes: String){
        this.notes.set(notes)
    }

    // Called on clicking the edit icon
    fun startEdit(){
        editing.set(true)
    }

    // Called on clicking the close icon
    fun stopEdit(){
        editing.set(false)
    }

    // Sets the details of the location to be updated in the view
    fun setDetails(location: CustomLocation){
        name.set(location.locationName)
        notes.set(location.locationNotes)
        latitude.set(location.latitude)
        longitude.set(location.longitude)
        address.set(location.address)
        id = location.id.toString()
    }


}