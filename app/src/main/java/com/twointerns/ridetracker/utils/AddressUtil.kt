package com.twointerns.ridetracker.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class AddressUtil {
    companion object {
        suspend fun getAddress(context: Context, latLng: LatLng?): String? {
            val geoCoder = Geocoder(context)
            val addresses: List<Address>?
            val address: Address?
            var addressText :String ?= null

            try {
                addresses = geoCoder.getFromLocation(latLng?.latitude!!, latLng.longitude, 1)
                if (null != addresses && addresses.isNotEmpty()) {
                    address = addresses[0]
                    addressText=address.getAddressLine(0)
//                    for (i in 0 until address.maxAddressLineIndex) {
//                        addressText += if (i == 0) address.getAddressLine(i) else "\n"+
//                                address.getAddressLine(i)
//                    }
                }
                Log.e("address", addressText)

            } catch (e: IOException) {
                Log.e("address", e.localizedMessage)
            }

            return addressText
        }

    }

}