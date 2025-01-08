package com.ejecicio.myapplication.scenes.partakers

data class University(
    val id: Int,
    val nombre: String,
    val correo_admin: String,
    val correo_estudiantes: String,
    val programas_intercambio: List<String>
)
