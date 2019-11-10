package com.twointerns.ridetracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class RecoverViewModel(application: Application) : AndroidViewModel(application) {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _error= MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var _success= MutableLiveData<String>()
    val success: LiveData<String>
        get() = _success

    private var _empty= MutableLiveData<String>()
    val empty: LiveData<String>
        get() = _empty

    fun sendRecoverLink(email: String) {
        if (email.isNullOrEmpty()) {
            _empty.value="shown"
        } else {
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("recoverViewModel", "success")
                        _success.value="shown"
                    }

                }
                .addOnFailureListener {
                    Log.d("recoverViewModel", "Error: ${it}")
                    _error.value="${it.localizedMessage}"
                }
        }
    }
}