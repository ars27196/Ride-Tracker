package com.twointerns.ridetracker.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*


class UploadFirebase {
    companion object {
        private lateinit var database: DatabaseReference

        private var _success: MutableLiveData<Boolean> = MutableLiveData()
        val success:LiveData<Boolean>
            get() = _success


        fun uploadToServer() {
            database = FirebaseDatabase.getInstance().reference
            val entity = database.child("plane")
//            entity.child(plane.planeId).setValue(plane)
            entity.push().addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    _success.value = false
                }

                override fun onDataChange(p0: DataSnapshot) {
                    _success.value = true
                }

            })

        }
    }
}