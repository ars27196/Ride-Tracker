package com.twointerns.ridetracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LogInViewModel : ViewModel() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error

    private var _success: MutableLiveData<Boolean> = MutableLiveData()
    val success: LiveData<Boolean>
        get() = _success

    private var _failure: MutableLiveData<String> = MutableLiveData()
    val failure: LiveData<String>
        get() = _failure

    private var _showDialog: MutableLiveData<Boolean> = MutableLiveData()
    val showDialog: LiveData<Boolean>
        get() = _showDialog

    fun logIn(email:String?,password:String?){

        if (!email.isNullOrEmpty()&& !password.isNullOrEmpty()) {
            _showDialog.value=true
            mAuth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener {

                if (it.isSuccessful ) {
                    Log.d("viewmodel", "success")

                    _success.value=it.isSuccessful
                    _showDialog.value=false

                }
            }
                .addOnFailureListener{
                    Log.d("viewmodel", "Exception: ${it}")
                    _showDialog.value=false
                    _failure.value="${it.localizedMessage}"

                }


        }
        else{
            _error.value="show"

        }
    }


}
