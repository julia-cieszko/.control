package com.example.dermatologyapp.Models

import com.google.firebase.Timestamp

data class Lesion(
    val id: String? = "",
    val location: MutableList<Double>? = mutableListOf(),
    val bodySide: String? = "",
    val symptoms: List<String> = mutableListOf(),
    val photoUrl: MutableList<String>? = mutableListOf(),
    val photosTimestamps: MutableList<Timestamp>? = mutableListOf(),
    val diagnostics: String? = "",
    val quality: List<Double>? = listOf()
)
