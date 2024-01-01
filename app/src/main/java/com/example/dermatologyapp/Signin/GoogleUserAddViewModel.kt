package com.example.dermatologyapp.Signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermatologyapp.Models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class GoogleUserAddViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    fun addGoogleUserToFirestore(user: User) {
        viewModelScope.launch {
            try {
                db.collection("users").document(user.id!!).set(user)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
}