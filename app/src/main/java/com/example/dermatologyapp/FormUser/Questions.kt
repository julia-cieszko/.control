package com.example.dermatologyapp.FormUser

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermatologyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


//klasa opisujaca z czego sklada sie pojedyncze pytanie
data class FormQuestion(
    val index: Int,
    val text: String,
    var punctation: Int
)

//lista pytan
val formQuestionsTexts = listOf(
    R.string.question1,
    R.string.question2,
    R.string.question3,
    R.string.question4,
    R.string.question5,
    R.string.question6,
    R.string.question7,
    R.string.question8,
    R.string.question9,
    R.string.question10
)

//viewmodel na pytania
class QuestionViewModel(application: Application) : AndroidViewModel(application){
    val formQuestions = mutableStateListOf<FormQuestion>()
    private val auth = FirebaseAuth.getInstance()
    private val authUser = auth.currentUser
    private val db = FirebaseFirestore.getInstance()

    fun initializeQuestions(questionTexts: List<Int>) {
        viewModelScope.launch {
            formQuestions.clear()

            questionTexts.forEachIndexed{index, questionTextRes ->
                val questionText =
                    try {
                        getApplication<Application>().applicationContext.getString(questionTextRes)
                    }catch (e: Exception){
                        ""
                    }
                formQuestions.add(FormQuestion(index, questionText, 0))
            }
        }
    }

    fun uploadAnwers() {
        viewModelScope.launch {
            val answers = mutableListOf<Int>()
            for (formQuestion in formQuestions) {
                answers.add(formQuestion.punctation)
            }
            try {
                db.collection("users")
                    .document(authUser?.uid ?: "")
                    .update("riskFormPunctation", answers)
                    .addOnSuccessListener { result ->
                        Log.d("RISK_FORM", "updated: $result")
                    }
                    .addOnFailureListener{ exception ->
                        Log.d("RISK_FORM_EXCEPTION", "risk form exception: $exception")
                    }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
    init {
        initializeQuestions(formQuestionsTexts)
    }
}

