package com.twointerns.ridetracker.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.twointerns.ridetracker.R
import com.twointerns.ridetracker.database.LocationRoomDatabase
import com.twointerns.ridetracker.model.entity.LocationData
import com.twointerns.ridetracker.utils.AddressUtil
import com.twointerns.ridetracker.utils.GlobalUtils
import com.twointerns.ridetracker.view.activity.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LocationService : Service() {
    private val FASTEST_INTERVAL: Long = 1000 * 10
    private val INTERVAL: Long = 1000 * 10
    private val locationRequest = LocationRequest()
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationData: LocationData
    private var listofLocations=ArrayList<LatLng>()
    private lateinit var rideTrackerId :String
    private val rootJob = SupervisorJob()

    val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action?.equals(GlobalUtils.STARTFOREGROUND_ACTION)!!) {
            locationData=LocationData()
            rideTrackerId=intent.extras.getString(GlobalUtils.RIDE_TRACKER_ID)
            if(rideTrackerId!=null){
                locationData.rideId=rideTrackerId.toInt()
            }

            val notificationIntent = Intent(this, HomeActivity::class.java)
            notificationIntent.setAction(GlobalUtils.MAIN_ACTION)
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val icon = BitmapFactory.decodeResource(resources, R.drawable.biker_logo)
            val notification = NotificationCompat.Builder(this)
                .setContentTitle("Ride Tracker")
                .setTicker("Ride Tracker")
                .setContentText("Tracking your ride ")
                .setSmallIcon(R.drawable.biker_logo)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build()
            startForeground(
                GlobalUtils.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification
            )

            startLocationUpdates()

        } else if (intent.action == GlobalUtils.STOPFOREGROUND_ACTION
        ) {
            stopLocationUpdates()

        }

        return START_STICKY
    }

    private fun startLocationUpdates() {
        locationRequest.apply {
            interval = INTERVAL
            fastestInterval = FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult?) {
                listofLocations.add(LatLng(location?.lastLocation?.latitude!!,location.lastLocation?.longitude!!))
                Toast.makeText(
                    applicationContext,
                    "Location Update: " + listofLocations.size,
                    Toast.LENGTH_LONG
                ).show()


                Log.d("location",""+location?.locations.toString())

                super.onLocationResult(location)
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }
        }

        FusedLocationProviderClient(this).requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )


    }

    private fun stopLocationUpdates() {
        FusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
            .addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(
                    applicationContext,
                    "Location Removed: ${it.isSuccessful}" ,
                    Toast.LENGTH_LONG
                ).show()
                Log.d("location","success")

                locationData.latlngList=listofLocations
                CoroutineScope(coroutineContext).launch {

                LocationRoomDatabase.getDatabase(applicationContext).locationDao().insert(locationData)
                val startAddress=AddressUtil.getAddress(applicationContext,listofLocations.first())
                val stopAddress=AddressUtil.getAddress(applicationContext,listofLocations.last())
                LocationRoomDatabase.getDatabase(applicationContext).locationDao().updateStartAddress(startAddress,locationData.rideId!!)
                LocationRoomDatabase.getDatabase(applicationContext).locationDao().updateStopAddress(stopAddress,locationData.rideId!!)
                stopForeground(true)
                stopSelf()
                }
            }else{
                Toast.makeText(
                    applicationContext,
                    "Location Removed:  ${it.isSuccessful}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("location","failure")
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}