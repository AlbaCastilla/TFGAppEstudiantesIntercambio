package com.ejecicio.myapplication.scenes.authentication

import com.google.firebase.Timestamp

data class User (

    val uid:String,
    val name:String,
    val lastname:String,
    val age: String,
    val email:String,
    val role:String ="Student",
    val university:String = "",
    val homeCountry:String = "",
    val homeCity:String = "",
    val description:String = "",
    val hobbies: List<String> = listOf(),
    val city:String = "",

    )




