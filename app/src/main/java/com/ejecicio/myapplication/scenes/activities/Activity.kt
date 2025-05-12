package com.ejecicio.myapplication.scenes.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog

data class Activity (

    val uid:String,
    val title:String,
    val category: String,
    val description: String,
    val price: Float,
    val maxPeople: Int,
    val date: String,
    val time: String,
    val duration: String,
    val locationLink: String,
    val otherInfo: String,
    val peopleAdded: Int = 0,
    val creator: String,
    val city: String,

)

