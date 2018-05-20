package com.mylocations.viewmodels

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.location.Address
import android.support.test.runner.AndroidJUnit4
import com.mylocations.addlocation.AddLocationViewModel
import com.mylocations.dao.Db
import com.mylocations.extensions.getValueBlocking
import com.mylocations.repository.Repository
import com.mylocations.utils.Config
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import java.util.*

@RunWith(RobolectricTestRunner::class)
class AddLocationTest : Db(){

    private lateinit var viewModel: AddLocationViewModel

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Before
    fun create(){
        val repository = Repository(localRepository, remoteRepository)
        viewModel = AddLocationViewModel()
        viewModel.repository = repository
    }

    @Test
    fun saveLocationTest(){
        viewModel.saveLocation()
        assertEquals(viewModel.nameError.value, Config.LOCATION_NAME_ERROR)
        viewModel.name.set("Sydney")
        viewModel.notes.set("Sydney notes")
        val address = Address(Locale.getDefault())
        address.latitude = 13.000
        address.longitude = 20.111
        viewModel.setAddress(address)
        viewModel.saveLocation()
        val locationLiveData = appDatabase.localLocationModel().getLocation("1")
        val locationFromDb = locationLiveData.getValueBlocking()
        assertEquals(locationFromDb?.locationName, viewModel.name.get())
    }
}