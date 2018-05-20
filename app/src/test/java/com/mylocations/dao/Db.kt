package com.mylocations.dao

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import com.mylocations.repository.local.AppDatabase
import com.mylocations.repository.local.LocalRepository
import com.mylocations.repository.remote.RemoteRepository
import com.mylocations.utils.Config
import org.junit.After
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

abstract class Db {

    lateinit var appDatabase: AppDatabase
    lateinit var localRepository: LocalRepository
    lateinit var remoteRepository: RemoteRepository
    lateinit var preferences: SharedPreferences

    @Before
    fun setup(){
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).allowMainThreadQueries().build()
        preferences = InstrumentationRegistry.getContext().getSharedPreferences(Config.APP_PREFERENCE, Context.MODE_PRIVATE)
        localRepository = LocalRepository(appDatabase, preferences)
        remoteRepository = RemoteRepository(InstrumentationRegistry.getContext().assets)
    }
}