package com.twointerns.ridetracker.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Camera
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionUtil {


    companion object {

        val LOCATION_PERMISSION_CODE = 9367

        fun isLocationPermissionGiven(act: Activity): Boolean {
            return if (Build.VERSION.SDK_INT < 23) {
                true
            } else {
                act.permission(act,android.Manifest.permission.ACCESS_FINE_LOCATION) && act.permission(act,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }


        fun requestLocationPermission(frag: Fragment) {
            frag.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_CODE
            )
        }


        @RequiresApi(Build.VERSION_CODES.M)
        private fun Activity.permission(act: Activity, perm: String): Boolean {
            return ActivityCompat.checkSelfPermission( act,perm) == PackageManager.PERMISSION_GRANTED
        }
    }
}