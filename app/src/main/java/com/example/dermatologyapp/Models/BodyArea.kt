package com.example.dermatologyapp.Models

import com.google.firebase.Timestamp

data class BodyArea(
    val id: String? = "",
    val name: String? = "",
    val photoUrls: MutableList<String>? = mutableListOf(),
    val photoTimestamps: MutableList<Timestamp>? = mutableListOf()
)
