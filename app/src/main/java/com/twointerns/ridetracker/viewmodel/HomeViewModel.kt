package com.twointerns.ridetracker.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import com.twointerns.ridetracker.service.LocationService
import com.twointerns.ridetracker.utils.GlobalUtils
import com.twointerns.ridetracker.utils.TwoButtonAlert
import java.util.*


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val _application = application
    val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(_application)
    val editor: SharedPreferences.Editor = pref.edit()

    private var serviceStarted: Boolean = false
    private var _serviceRunning = MutableLiveData<Boolean>()
    val serviceRunning: LiveData<Boolean>
        get() = _serviceRunning

    init {
        _serviceRunning.value = pref.getBoolean(GlobalUtils.SERVICE_BUTTON_KEY, false)
    }

    fun foregroundLocationService() {
        serviceStarted = pref.getBoolean(GlobalUtils.SERVICE_STARTED_KEY, false)
        val startIntent = Intent(_application, LocationService::class.java)
        if (!serviceStarted) {
            TwoButtonAlert.newInstance("Confirm Ride", "Are you sure you want to Start ",
                "Yes", "No", {
                    _serviceRunning.value = true
                    editor.putBoolean(GlobalUtils.SERVICE_STARTED_KEY, true)
                    editor.putBoolean(GlobalUtils.SERVICE_BUTTON_KEY, _serviceRunning.value!!)
                    editor.commit()
                    val extras = Bundle()
                    extras.putString(GlobalUtils.RIDE_TRACKER_ID, "" + Random().nextInt())
                    startIntent.putExtras(extras)
                    startIntent.action = GlobalUtils.STARTFOREGROUND_ACTION
                    _application.startService(startIntent)
                }, {
                    //dismiss the dialog
                })


        } else {
            TwoButtonAlert.newInstance("Confirm Ride", "Are you sure you want to Start ",
                "Yes", "No", {
                    _serviceRunning.value = false
                    editor.putBoolean(GlobalUtils.SERVICE_STARTED_KEY, false)
                    editor.putBoolean(GlobalUtils.SERVICE_BUTTON_KEY, _serviceRunning.value!!)
                    editor.commit()
                    startIntent.action = GlobalUtils.STOPFOREGROUND_ACTION
                    _application.startService(startIntent)
                }, {

                })

        }
    }
}