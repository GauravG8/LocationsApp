package com.mylocations.repository.remote

/**
 * Data class for default location
 * @param name - location name
 * @param lat - location latitude
 * @param lng - location longitude
  */
data class ResponseLocationModel(var name: String, var lat: Double, var lng: Double)