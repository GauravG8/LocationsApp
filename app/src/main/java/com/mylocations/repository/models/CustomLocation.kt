package com.mylocations.repository.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Entity class for CustomLocation table - Room architecture component.
 * @param id - primary key
 * @param locationName - location name
 * @param locationNotes - location notes
 * @param latitude - location latitude
 * @param longitude - location longitude
 * @param address - address in string
 */
@Entity
class CustomLocation(var locationName: String?, var locationNotes: String?, var latitude: Double, var longitude: Double, var address: String?) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
