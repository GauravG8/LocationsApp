package com.mylocations.viewmodels

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.mylocations.dao.Db
import com.mylocations.extensions.getValueBlocking
import com.mylocations.map.MapViewModel
import com.mylocations.repository.Repository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MapUnitTest : Db() {
    private lateinit var viewModel: MapViewModel

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Before
    fun create(){
        val repository = Repository(localRepository, remoteRepository)
        viewModel = MapViewModel()
        viewModel.repository = repository
    }

    @Test
    fun appStartTest(){
        viewModel.appStart()
        var countLiveData = viewModel.getItemCount()
        var countFromDb = countLiveData?.getValueBlocking()
        assertEquals(countFromDb, 5.toString())
        viewModel.appStart()
        countLiveData = viewModel.getItemCount()
        countFromDb = countLiveData?.getValueBlocking()
        assertEquals(countFromDb, 5.toString())
    }
}