package com.example.dermatologyapp.Models

data class User(
    val id: String? = "",
    val name: String? = "",
    val birthYear: Int? = 0,
    val gender: String? = "",
    val bodyAreasList: MutableList<BodyArea>? = mutableListOf(),
    val lesions: MutableList<String>? = mutableListOf(),
    val riskFormPunctation: MutableList<Int>? = mutableListOf()
)
