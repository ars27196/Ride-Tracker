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
        fun getAddress(context: Context, latLng: LatLng?): LiveData<String >{
            val geoCoder = Geocoder(context)
            val addresses: List<Address>?
            val address: Address?
            var addressText = MutableLiveData<String>()

            try {
                addresses = geoCoder.getFromLocation(latLng?.latitude!!, latLng.longitude, 1)
                if (null != addresses && addresses.isNotEmpty()) {
                    address = addresses[0]
                    for (i in 0 until address.maxAddressLineIndex) {
                        addressText.value += if (i == 0) address.getAddressLine(i) else "\n"+
                                address.getAddressLine(i)
                    }
                }
            } catch (e: IOException) {
                Log.e("address", e.localizedMessage)
            }

            return addressText
        }

    }

}