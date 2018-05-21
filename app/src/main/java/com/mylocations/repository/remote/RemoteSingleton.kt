package com.mylocations.repository.remote

import android.content.Context

/**
 * Singleton class to retrieve the global instance for RemoteRepository
 */
class RemoteSingleton {

    companion object {
        private var remoteRepository : RemoteRepository? = null

        //Function to get the singleton instance of RemoteRepository
        fun getInstance(context : Context) : RemoteRepository?{
            if (remoteRepository == null){
                synchronized(RemoteRepository::class){
                    remoteRepository = RemoteRepository(context.assets);
                }
            }
            return remoteRepository
        }
    }
}