package com.example.dermatologyapp.Signin

data class RegistrationUIState(
    var name: String = "",
    var birthYear: String = "2023",
    var gender: String = "kobieta",
    var email: String = "",
    var password: String = "",

    var nameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,

)
