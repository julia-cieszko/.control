package com.example.dermatologyapp.Signin

sealed class SignUpUIEvent {
    data class NameChanged(val name:String) : SignUpUIEvent()
    data class BirthYearChanged(val birthYear:String) : SignUpUIEvent()
    data class GenderChanged(val gender:String) : SignUpUIEvent()
    data class EmailChanged(val email:String) : SignUpUIEvent()
    data class PasswordChanged(val password:String) : SignUpUIEvent()

    object RegisterButtonClicked : SignUpUIEvent()

}