package com.ejecicio.myapplication.scenes.messages

data class Chat (
    val uid:String,//uid of chat
    val activity:String,//uid of activity
    val creator: String,//uid of creator of activity
    val participants:List<String>,
)




