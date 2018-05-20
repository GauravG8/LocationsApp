package com.mylocations.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.mylocations.extensions.getValueBlocking
import com.mylocations.repository.local.AppDatabase
import com.mylocations.repository.models.CustomLocation
import com.mylocations.utils.Config
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

@RunWith(AndroidJUnit4::class)
open class LocationDaoTest : Db(){

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertLocationTest(){
        val location = CustomLocation("Opera house", "Opera house", 13.4323, 87.123, "Sydeny opera house, NSW, Australia", Config.LOCATION_TYPE_CUSTOM)
        val id = appDatabase.localLocationModel().addLocation(location)
        assertEquals(id, 1)
        val locationLiveData = appDatabase.localLocationModel().getLocation(id.toString())
        val locationFromDb = locationLiveData.getValueBlocking()
        assertEquals(locationFromDb?.locationName, location.locationName)
    }

    @Test
    fun updateLocationNotesTest(){
        val location = CustomLocation("Opera house", "Opera house", 13.4323, 87.123, "Sydeny opera house, NSW, Australia", Config.LOCATION_TYPE_CUSTOM)
        val id = appDatabase.localLocationModel().addLocation(location)

        appDatabase.localLocationModel().updateNotes(id.toString(), "Sydney Opera House")
        val locationLiveData = appDatabase.localLocationModel().getLocation(id.toString())
        val locationFromDb = locationLiveData.getValueBlocking()
        assertEquals(locationFromDb?.locationNotes, "Sydney Opera House")
    }

    @Test
    fun deleteLocationTest(){
        val location = CustomLocation("Opera house", "Opera house", 13.4323, 87.123, "Sydeny opera house, NSW, Australia", Config.LOCATION_TYPE_CUSTOM)
        val id = appDatabase.localLocationModel().addLocation(location)

        var locationLiveData = appDatabase.localLocationModel().getLocation(id.toString())
        var locationFromDb = locationLiveData.getValueBlocking()

        appDatabase.localLocationModel().deleteLocation(locationFromDb!!)
        locationLiveData = appDatabase.localLocationModel().getLocation(id.toString())
        locationFromDb = locationLiveData.getValueBlocking()
        assertNull(locationFromDb)
    }

    @Test
    fun getUserTest(){
        val location = CustomLocation("Opera house", "Opera house", 13.4323, 87.123, "Sydeny opera house, NSW, Australia", Config.LOCATION_TYPE_CUSTOM)
        val id = appDatabase.localLocationModel().addLocation(location)

        val locationLiveData = appDatabase.localLocationModel().getLocation(id.toString())
        val locationFromDb = locationLiveData.getValueBlocking()
        assertEquals(locationFromDb?.locationName, location.locationName)
    }

    @Test
    fun getAllUsers(){
        var location = CustomLocation("Opera house", "Opera house", 13.4323, 87.123, "Sydeny opera house, NSW, Australia", Config.LOCATION_TYPE_CUSTOM)
        var id = appDatabase.localLocationModel().addLocation(location)

        location = CustomLocation("Opera house1", "Opera house1", 13.4323, 87.123, "Sydeny opera house, NSW, Australia", Config.LOCATION_TYPE_CUSTOM)
        id = appDatabase.localLocationModel().addLocation(location)

        val locationLiveData = appDatabase.localLocationModel().getAllLocations()
        val locationsFromDb = locationLiveData.getValueBlocking()
        assertEquals(locationsFromDb?.size, 2)
    }




}