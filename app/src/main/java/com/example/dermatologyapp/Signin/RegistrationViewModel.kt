package com.example.dermatologyapp.Signin

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.dermatologyapp.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//ViewModel na zwykłe logowanie
class RegistrationViewModel() : ViewModel() {

    var registrationUIState = mutableStateOf(RegistrationUIState())

    private val _signUpInProgress = MutableStateFlow(true)
    val signUpInProgress = _signUpInProgress.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun onEvent(event: SignUpUIEvent) {
        when(event) {
            is SignUpUIEvent.NameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    name = event.name
                )
            }

            is SignUpUIEvent.BirthYearChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    birthYear = event.birthYear
                )
            }

            is SignUpUIEvent.GenderChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    gender = event.gender
                )
            }

            is SignUpUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
            }

            is SignUpUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
            }

            is SignUpUIEvent.RegisterButtonClicked -> {
                signUp()
            }

            else -> {}
        }
    }

    private fun signUp() {
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password,
            name = registrationUIState.value.name,
            birthYear = registrationUIState.value.birthYear.toInt(),
            gender = registrationUIState.value.gender
        )
    }

    private fun createUserInFirebase(email: String, password: String, name: String, birthYear: Int, gender: String) {

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_OnCompleteListener")
                Log.d(TAG, "isSuccessful = ${it.isSuccessful}")
                try {
                    db.collection("users").document(it.result.user!!.uid).set(
                        User (
                            id = it.result.user?.uid,
                            name = name,
                            birthYear = birthYear,
                            gender = gender
                        )
                    )
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                _signUpInProgress.value = false
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_OnFailureListener")
                Log.d(TAG, "Exception = ${it.message}")
                _signUpInProgress.value = false
            }
    }
}