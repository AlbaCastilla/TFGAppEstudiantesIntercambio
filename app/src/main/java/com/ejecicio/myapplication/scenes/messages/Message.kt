package com.ejecicio.myapplication.scenes.messages

data class Message(
    val uid: String = "",             // Proporcionar valores predeterminados
    val timestamp: String = "",
    val info: String = "",
    val userId: String = "",
    val userName: String = ""
) {
    // Constructor sin argumentos requerido por Firebase
    constructor() : this("", "", "", "", "")
}





