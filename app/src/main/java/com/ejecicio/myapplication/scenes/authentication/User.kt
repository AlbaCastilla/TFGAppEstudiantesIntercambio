package com.ejecicio.myapplication.scenes.authentication

data class User (

    val uid:String,
    val name:String,
    val lastname:String,
    val age:Int,
    val email:String,
    val role:String ="Student",
    val university:String = "",
    val homeCountry:String = "",
    val homeCity:String = "",
    val description:String = "",
    val hobbies: List<String> = listOf(),
    val city:String = "",

)




