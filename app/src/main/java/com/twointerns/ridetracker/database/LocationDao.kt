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

    @Query("UPDATE location_data SET startLocationAddress = :address WHERE rideId =:id")
    fun updateStartAddress(address :String?, id:Int)

    @Query("UPDATE location_data SET lastLocationAddress = :address WHERE rideId =:id")
    fun updateStopAddress(address:String?, id:Int)

    @Delete
    fun deleteLocationData(locationData: LocationData)

}