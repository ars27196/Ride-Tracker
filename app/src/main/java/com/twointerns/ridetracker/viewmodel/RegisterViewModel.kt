package com.twointerns.ridetracker.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val context: Context = application
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _success: MutableLiveData<Boolean> = MutableLiveData()
    val sucess: LiveData<Boolean>
        get() = _success

    private var _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error

    private var _userAlreadyCreated: MutableLiveData<String> = MutableLiveData()
    val userAlreadyCreated: LiveData<String>
        get() = _userAlreadyCreated

    private var _showDialog: MutableLiveData<Boolean> = MutableLiveData()
    val showDialog: LiveData<Boolean>
        get() = _showDialog


    fun registerAccount(email: String?, password: String?) {

        _showDialog.value = true
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                if (it.isSuccessful) {
                    _showDialog.value = false
                    _success.value=true
                    Log.d("viewmodel", "success")
                }
            }
                .addOnFailureListener {
                    Log.d("viewmodel", "Exception: ${it}")

                    _userAlreadyCreated.value = it.localizedMessage

                }


        } else {
            _error.value = "show"

        }
    }
}
