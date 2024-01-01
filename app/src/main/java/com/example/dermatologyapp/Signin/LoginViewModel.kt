package com.example.dermatologyapp.Signin

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())
    var loginInProgress = mutableStateOf(false)

    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful = _isLoginSuccessful.asStateFlow()

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                logIn()
            }
        }
    }

    private fun logIn() {
        loginInProgress.value = true
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(
                loginUIState.value.email,
                loginUIState.value.password
            )
            .addOnCompleteListener {
                Log.d(ContentValues.TAG, "Inside_OnCompleteListener")
                Log.d(ContentValues.TAG, "isSuccessful = ${it.isSuccessful}")
                loginInProgress.value = false
                _isLoginSuccessful.value = true
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Inside_OnFailureListener")
                Log.d(ContentValues.TAG, "Exception = ${it.message}")
                loginInProgress.value = false
            }

    }
}