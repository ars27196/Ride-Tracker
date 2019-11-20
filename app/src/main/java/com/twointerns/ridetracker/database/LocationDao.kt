package com.twointerns.ridetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.twointerns.ridetracker.model.entity.LocationData

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(locationData: LocationData)

    @Update
    fun update(locationData: LocationData)

    @Query("SELECT * from location_data ORDER BY rideId ASC")
    fun retrieveLocationData(): LiveData<List<LocationData>>
}