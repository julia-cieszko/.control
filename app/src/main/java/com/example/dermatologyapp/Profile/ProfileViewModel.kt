package com.example.dermatologyapp.Profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermatologyapp.Models.Lesion
import com.example.dermatologyapp.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val authUser = auth.currentUser
    private val db = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _lesions = MutableStateFlow<List<Lesion>>(listOf())
    val lesions = _lesions.asStateFlow()

    fun loadUserData() {
        viewModelScope.launch {
            val uid = authUser?.uid
            try {
                db.collection("users")
                    .document(uid!!)
                    .get()
                    .addOnSuccessListener { result ->
                        val newUser = result.toObject<User>()
                        _user.value = newUser
                        Log.d("USERDATA", "data: $newUser")
                        loadLesions()
                    }
                    .addOnFailureListener{ exception ->
                        Log.d("USERDATA", "exception: $exception")
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadLesions() {
        viewModelScope.launch {
            val lesionsList = mutableListOf<Lesion>()
            try {
                Log.d("LESIONS", "data: ${_user.value!!.lesions}")
                for (lesionId in _user.value!!.lesions!!) {
                    db.collection("lesions")
                        .document(lesionId)
                        .get()
                        .addOnSuccessListener { result ->
                            val newLesion = result.toObject<Lesion>()
                            lesionsList.add(newLesion!!)
                            _lesions.value = lesionsList
                            Log.d("LESIONS", "data: $newLesion")
                        }
                        .addOnFailureListener{ exception ->
                            Log.d("LESIONS", "exception: $exception")
                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}