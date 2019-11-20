package com.twointerns.ridetracker.utils

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocationTypeConverter{
    @TypeConverter
    fun storedStringToMyObjects(data: String?): LatLng {
        val gson = Gson()
        if (data == null) {
            return LatLng(0.0,0.0)
        }
        val variableType = object : TypeToken<LatLng>() {

        }.type
        return gson.fromJson<LatLng>(data, variableType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: LatLng): String {
        val gson = Gson()
        return gson.toJson(myObjects)
    }
}