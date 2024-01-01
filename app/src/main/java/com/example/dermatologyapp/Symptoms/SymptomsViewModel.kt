package com.example.dermatologyapp.Symptoms

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class SymptomsViewModel : ViewModel(){
    private val _selectedSymptoms = mutableStateListOf<String>() //lista wybranych objawow - stan
    val selectedSymptoms: List<String> get() = _selectedSymptoms //lista wybranych objawow

    //funkcja odpowiadajaca za zaznaczanie i odznaczanie
    fun toggleSymptom(symptom: String) {
        if (_selectedSymptoms.contains(symptom)) {
            _selectedSymptoms.remove(symptom)
        } else {
            _selectedSymptoms.add(symptom)
        }
    }

}