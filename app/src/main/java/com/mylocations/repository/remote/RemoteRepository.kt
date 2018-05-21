package com.mylocations.repository.remote

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.mylocations.repository.OnDataReadyCallback
import com.mylocations.utils.Config
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Remote Repository acts as the medium to communicate to web services and returns the
 * results to main Repository
 */
class RemoteRepository(val asset : AssetManager) {

    //Executor for performing work in a separate thread.
    private val executor: Executor

    init {
        executor = Executors.newSingleThreadExecutor()
    }

    /**
     * Convert json string to ResponseModel object and notify the repository.
     */
    fun getLocationsFromFile(onRemoteDataReadyCallback: OnRemoteDataReadyCallback){
        executor.execute( {
            val gson = Gson()
            val responseModel : ResponseModel = gson.fromJson(readJSONFromAssets(), ResponseModel::class.java)
            onRemoteDataReadyCallback.onRemoteDataReady(responseModel)
        })
    }

    /**
     * Utility function to read the json file from assets and return string.
     */
    fun readJSONFromAssets() : String{
        val inputStream = asset.open("locations.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}

/**
 * Callback interface to notify the calling class after importing json file to object
 */
interface OnRemoteDataReadyCallback{
    fun onRemoteDataReady(responseModel: ResponseModel)
}