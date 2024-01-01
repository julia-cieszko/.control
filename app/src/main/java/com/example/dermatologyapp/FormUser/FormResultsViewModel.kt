package com.example.dermatologyapp.FormUser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class FormResultsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _answers = MutableStateFlow(listOf<Int>())
    val answers = _answers.asStateFlow()
    private val auth = FirebaseAuth.getInstance()
    private val authUser = auth.currentUser
    private val _gender = MutableStateFlow("")
    private val _age = MutableStateFlow(0)
    val gender = _gender.asStateFlow()
    val age = _age.asStateFlow()
    fun loadAnswersFromFirestore() {
        viewModelScope.launch {
            try {
                db.collection("users")
                    .document(authUser?.uid?:"")
                    .get()
                    .addOnSuccessListener { result ->
                        val temp = result.data
                        _answers.value = temp?.get("riskFormPunctation") as List<Int>
                        _gender.value = temp["gender"] as String
                        val year = temp["birthYear"] as Long
                        val sdf = SimpleDateFormat("yyyy")
                        val date = sdf.format(Date())
                        _age.value = date.toInt() - year.toInt()
                        Log.d("AGE :)", "age: ${_age.value}")
                        Log.d("ANSWERS", "answers: ${_answers.value}")
                    }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
}