package com.mylocations.repository.local

import android.content.Context
import com.mylocations.utils.Config

/**
 * Singleton class to retrieve the global instance for LocalRepository
 */
class LocalSingleton {

    companion object {
        private var localRepository : LocalRepository? = null

        //Function to get the singleton instance of LocalRepository
        fun getInstance(context : Context) : LocalRepository?{
            if (localRepository == null){
                synchronized(LocalRepository::class){
                    localRepository = LocalRepository(AppDatabase.getInstance(context), context.getSharedPreferences(Config.APP_PREFERENCE, Context.MODE_PRIVATE));
                }
            }
            return localRepository
        }
    }
}