package com.twointerns.ridetracker.utils

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.Collections.emptyList



class ListTypeConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<LatLng> {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<LatLng>>() {

        }.type
        return gson.fromJson<List<LatLng>>(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<LatLng>): String {
        val gson = Gson()
        return gson.toJson(myObjects)
    }
}