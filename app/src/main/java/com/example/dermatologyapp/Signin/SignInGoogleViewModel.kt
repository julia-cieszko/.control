package com.example.dermatologyapp.Signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInGoogleViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInGoogleState())
    val state = _state.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    private val _userExists = MutableStateFlow(false)
    val userExists = _userExists.asStateFlow()

    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            _state.update { it.copy(
                signInError = result.errorMessage
            ) }
            _id.value = result.data?.userId ?: ""
            _name.value = result.data?.userName ?: ""
        }
    }

    fun resetState() {
        _state.update { SignInGoogleState() }
    }

    fun doesGoogleUserExist(uid: String) {
        viewModelScope.launch {
            try {
                Log.d("UID", "uid: $uid")
                val query = db.collection("users").document(uid).get().await()
                _userExists.value = query.exists()
                Log.d("USER_EXISTS", query.exists().toString())
                _state.update { it.copy(
                    isSignInSuccessful = true,
                ) }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
}