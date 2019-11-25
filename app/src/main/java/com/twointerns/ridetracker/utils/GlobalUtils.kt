package com.twointerns.ridetracker.utils

class GlobalUtils {


   companion object{
       val ApiKey = "AIzaSyABisSGwdgHRDoZUwNoW3iTi0QFHRCRHTA"
       val RIDE_TRACKER_ID: String="RIDE_TRACKER_ID"
       val MAIN_ACTION="com.twointerns.ridetracker.action.main"
       val STARTFOREGROUND_ACTION = "com.twointerns.ridetracker.action.startforeground"
       val STOPFOREGROUND_ACTION = "com.twointerns.ridetracker.action.stopforeground"
       val SERVICE_STARTED_KEY="SERVICE_STARTED"
       val SERVICE_BUTTON_KEY="SERVICE_STARTED"
       var trafficEnabled=false
   }
    internal interface NOTIFICATION_ID {
        companion object {
            val FOREGROUND_SERVICE = 101

        }
    }
}
