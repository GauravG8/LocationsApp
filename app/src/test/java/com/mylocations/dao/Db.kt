package com.mylocations.dao

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import com.mylocations.repository.local.AppDatabase
import com.mylocations.repository.local.LocalRepository
import com.mylocations.repository.local.LocalSingleton
import com.mylocations.repository.remote.RemoteRepository
import com.mylocations.repository.remote.RemoteSingleton
import com.mylocations.utils.Config
import org.junit.After
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RuntimeEnvironment
import java.io.IOException
import java.lang.reflect.Field

abstract class Db {

    lateinit var appDatabase: AppDatabase
    var localRepository: LocalRepository? = null
    var remoteRepository: RemoteRepository? = null
    lateinit var preferences: SharedPreferences

    @Before
    fun setup(){
        appDatabase = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application.applicationContext, AppDatabase::class.java).allowMainThreadQueries().build()
        preferences = InstrumentationRegistry.getContext().getSharedPreferences(Config.APP_PREFERENCE, Context.MODE_PRIVATE)
        localRepository = LocalRepository(appDatabase, preferences)
        remoteRepository = RemoteRepository(InstrumentationRegistry.getContext().assets)
    }

    @After
    fun close(){
        appDatabase.close()
    }
}