package com.mylocations.viewmodels

import com.mylocations.detail.LocationDetailViewModel
import com.mylocations.repository.Repository
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.mylocations.dao.Db
import com.mylocations.extensions.getValueBlocking
import com.mylocations.repository.models.CustomLocation
import com.mylocations.utils.Config
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class LocationDetailsUnitTest : Db(){

    private lateinit var viewModel : LocationDetailViewModel

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Before
    fun create(){
        val repository = Repository(localRepository, remoteRepository)
        viewModel = LocationDetailViewModel()
        viewModel.repository = repository
    }

    @Test
    fun updateNotes_Test(){
        val location = insertLocation()
        viewModel.notes.set("Sydney Opera House")
        viewModel.location.set(location)
        viewModel.updateNotes()
        //val locationLiveData = appDatabase.localLocationModel().getLocation(location?.id.toString())
        val locationLiveData = viewModel.getLocation(location?.id.toString())
        val locationFromDb = locationLiveData?.getValueBlocking()
        assertEquals(locationFromDb?.locationNotes, "Sydney Opera House")
        assertEquals("Notes updated", viewModel.notesUpdated.value)
        assertEquals(false, viewModel.editing.get())
    }

    fun insertLocation() : CustomLocation?{
        val location = CustomLocation("Opera house", "Opera house", 13.4323, 87.123, "Sydeny opera house, NSW, Australia", Config.LOCATION_TYPE_DEFAULT)
        val id = appDatabase.localLocationModel().addLocation(location)
        val locationLiveData = viewModel.getLocation(id.toString())
        val locationFromDb = locationLiveData?.getValueBlocking()
        return locationFromDb
    }

}