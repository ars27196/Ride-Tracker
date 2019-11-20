package com.twointerns.ridetracker.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.twointerns.ridetracker.utils.ListTypeConverter
import com.twointerns.ridetracker.utils.LocationTypeConverter

@Entity(tableName = "location_data")
class LocationData {

    @PrimaryKey(autoGenerate = true)
    var rideId: Int? = null


    @TypeConverters(ListTypeConverter::class)
    var latlngList: List<LatLng>? = null


    @TypeConverters(LocationTypeConverter::class)
    var lastLocationLatLng: LatLng? = null
    get() = latlngList?.last()


    @TypeConverters(LocationTypeConverter::class)
    var startLocationLatLng: LatLng? = null
        get() =  latlngList?.first()


}