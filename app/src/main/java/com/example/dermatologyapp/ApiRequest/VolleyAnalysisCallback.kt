package com.example.dermatologyapp.ApiRequest

import com.example.dermatologyapp.Models.Lesion

interface VolleyAnalysisCallback {
    fun onSuccess(lesion: Lesion)
}