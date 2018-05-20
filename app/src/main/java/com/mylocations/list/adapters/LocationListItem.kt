package com.mylocations.list.adapters

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.location.Location
import com.mylocations.repository.models.CustomLocation

/**
 * Data class for Location List item
 * @param location - Location object
 * @param distance - distance from the current location
 * @param distanceString - distance in String format
 */
class LocationListItem(var location: CustomLocation, var distance: Float, var distanceString: String = "")