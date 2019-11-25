package com.twointerns.ridetracker.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.twointerns.ridetracker.utils.ListTypeConverter
import com.twointerns.ridetracker.utils.LocationTypeConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "location_data")
class LocationData :Parcelable{

    @PrimaryKey(autoGenerate = true)
    var rideId: Int? = null


    @TypeConverters(ListTypeConverter::class)
    var latlngList: List<LatLng>? = null


    var startLocationAddress: String? = null

    var lastLocationAddress: String? = null




}