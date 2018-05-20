package com.mylocations.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.view.View.GONE
import android.view.View.VISIBLE
import com.mylocations.MyApplication
import com.mylocations.repository.Repository
import com.mylocations.repository.models.CustomLocation
import com.mylocations.utils.Config

class LocationDetailViewModel : ViewModel() {

    var repository: Repository = MyApplication.getRepository()

    //Observable fields for data binding
    /*val name = ObservableField<String>()
    val notes = ObservableField<String>()
    val latitude = ObservableField<Double>()
    val longitude = ObservableField<Double>()
    val address = ObservableField<String>()*/
    val notes = ObservableField<String>()
    val location = ObservableField<CustomLocation>()
    val editing = ObservableField<Boolean>(false)

    // LiveData variables observed in the activity
    val notesUpdated = MutableLiveData<String>()
    /**
     * Gets the location from the database. This value is observed in the view
     */
    fun getLocation(id: String): LiveData<CustomLocation>? =  repository.getLocation(id);

    /**
     * Called when the user edits the notes of the location.
     * Updates the database and notifies the view.
     */
    fun updateNotes(){
        repository.updateNotes(location.get()?.id.toString(), notes.get())
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
        this.location.set(location)
        notes.set(location.locationNotes)
    }

    fun setStarVisibility() = if (location.get()?.type == Config.LOCATION_TYPE_DEFAULT) VISIBLE else GONE
}