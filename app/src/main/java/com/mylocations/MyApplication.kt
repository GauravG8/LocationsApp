package com.mylocations

import android.app.Application
import android.location.Geocoder
import com.mylocations.repository.Repository
import com.mylocations.repository.local.LocalSingleton
import com.mylocations.repository.remote.RemoteSingleton

class MyApplication : Application(){

    companion object {
        private lateinit var repository : Repository
        fun getRepository() = repository
    }

    override fun onCreate() {
        super.onCreate()
        repository = Repository(LocalSingleton.getInstance(applicationContext), RemoteSingleton.getInstance(applicationContext), Geocoder(applicationContext))
    }
}