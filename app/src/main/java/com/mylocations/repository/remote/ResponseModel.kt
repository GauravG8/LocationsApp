package com.mylocations.repository.remote

/**
 * Data class to convert json file to object using Gson
 * @param locations - list of locations
 * @param updated - time updated
  */
data class ResponseModel(var locations: List<ResponseLocationModel>, var updated: String)