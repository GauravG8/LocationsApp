package com.mylocations.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.mylocations.dao.Db
import com.mylocations.extensions.getValueBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class RepositoryTest : Db() {

    private lateinit var repository: Repository

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun create(){
        repository = Repository(localRepository, remoteRepository)
    }

    @Test
    fun getLocationFromFileTest(){
        repository.getLocationsFromFile(object : OnDataReadyCallback{
            override fun sendStatus(status: Boolean) {

            }
        })
        val locationsLiveData = appDatabase.localLocationModel().getAllLocations()
        val locationsFromDb = locationsLiveData.getValueBlocking()
        assertEquals(locationsFromDb?.size, 5)
    }

}