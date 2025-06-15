//Adding UNIS from the json

package com.ejecicio.myapplication.scenes.partakers

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.UUID

@Composable
fun AddUniversities(navController: NavHostController) {
    val dbFirestore = FirebaseFirestore.getInstance()

    // JSON data with random IDs
    val universitiesJson = """
    [
      {
        "nombre": "Universidad Nebrija",
        "correo_admin": "@nebrija.es",
        "correo_estudiantes": "@alumnos.nebrija.es",
        "programas_intercambio": ["Erasmus+", "Programas propios"]
      },
      {
        "nombre": "Tufts University",
        "correo_admin": "@tufts.edu",
        "correo_estudiantes": "@students.tufts.edu",
        "programas_intercambio": ["Exchange Programs", "Tufts-in-Spain"]
      },
      {
        "nombre": "Schiller International University",
        "correo_admin": "@schiller.edu",
        "correo_estudiantes": "@students.schiller.edu",
        "programas_intercambio": ["Global Mobility Program"]
      },
      {
        "nombre": "Universidad Complutense de Madrid",
        "correo_admin": "@ucm.es",
        "correo_estudiantes": "@alumnos.ucm.es",
        "programas_intercambio": ["Erasmus+", "Convenios Bilaterales"]
      },
      {
        "nombre": "Universidad Politécnica de Madrid",
        "correo_admin": "@upm.es",
        "correo_estudiantes": "@alumnos.upm.es",
        "programas_intercambio": ["Erasmus+", "Programas propios"]
      },
      {
        "nombre": "Universidad Autónoma de Madrid",
        "correo_admin": "@uam.es",
        "correo_estudiantes": "@estudiantes.uam.es",
        "programas_intercambio": ["Erasmus+", "Intercambios Internacionales"]
      },
      {
        "nombre": "Universidad Carlos III de Madrid",
        "correo_admin": "@uc3m.es",
        "correo_estudiantes": "@alumnos.uc3m.es",
        "programas_intercambio": ["Erasmus+", "Global Exchange"]
      },
      {
        "nombre": "Universidad de Alcalá",
        "correo_admin": "@uah.es",
        "correo_estudiantes": "@alumnos.uah.es",
        "programas_intercambio": ["Erasmus+", "Programas propios"]
      },
      {
        "nombre": "Universidad Rey Juan Carlos",
        "correo_admin": "@urjc.es",
        "correo_estudiantes": "@alumnos.urjc.es",
        "programas_intercambio": ["Erasmus+", "Intercambios Internacionales"]
      },
      {
        "nombre": "Universidad Pontificia Comillas",
        "correo_admin": "@comillas.edu",
        "correo_estudiantes": "@alumnos.comillas.edu",
        "programas_intercambio": ["Erasmus+", "Red Internacional"]
      },
      {
        "nombre": "Universidad Europea de Madrid",
        "correo_admin": "@uem.es",
        "correo_estudiantes": "@estudiantes.uem.es",
        "programas_intercambio": ["Erasmus+", "Global Exchange"]
      },
      {
        "nombre": "Universidad Alfonso X el Sabio",
        "correo_admin": "@uax.es",
        "correo_estudiantes": "@alumnos.uax.es",
        "programas_intercambio": ["Erasmus+", "Programas propios"]
      },
      {
        "nombre": "Universidad Francisco de Vitoria",
        "correo_admin": "@ufv.es",
        "correo_estudiantes": "@alumnos.ufv.es",
        "programas_intercambio": ["Erasmus+", "Convenios Internacionales"]
      },
      {
        "nombre": "IE University",
        "correo_admin": "@ie.edu",
        "correo_estudiantes": "@students.ie.edu",
        "programas_intercambio": ["Global Mobility", "Dual Degrees"]
      },
      {
        "nombre": "Universidad Camilo José Cela",
        "correo_admin": "@ucjc.edu",
        "correo_estudiantes": "@alumnos.ucjc.edu",
        "programas_intercambio": ["Erasmus+", "Intercambios Bilaterales"]
      },
      {
        "nombre": "Universidad de Salamanca (Campus de Madrid)",
        "correo_admin": "@usal.es",
        "correo_estudiantes": "@alumnos.usal.es",
        "programas_intercambio": ["Erasmus+", "Programas Propios"]
      },
      {
        "nombre": "Universidad a Distancia de Madrid (UDIMA)",
        "correo_admin": "@udima.es",
        "correo_estudiantes": "@estudiantes.udima.es",
        "programas_intercambio": ["Convenios Internacionales"]
      },
      {
        "nombre": "Universidad Villanueva",
        "correo_admin": "@villanueva.edu",
        "correo_estudiantes": "@students.villanueva.edu",
        "programas_intercambio": ["Erasmus+", "Programas Propios"]
      },
      {
        "nombre": "Escuela de Organización Industrial (EOI)",
        "correo_admin": "@eoi.es",
        "correo_estudiantes": "@estudiantes.eoi.es",
        "programas_intercambio": ["Global Exchange"]
      },
      {
        "nombre": "Centro Universitario Cardenal Cisneros",
        "correo_admin": "@cardenalcisneros.es",
        "correo_estudiantes": "@alumnos.cardenalcisneros.es",
        "programas_intercambio": ["Erasmus+", "Convenios Propios"]
      },
      {
        "nombre": "CUNEF Universidad",
        "correo_admin": "@cunef.edu",
        "correo_estudiantes": "@students.cunef.edu",
        "programas_intercambio": ["Erasmus+", "Global Programs"]
      },
      {
        "nombre": "Universidad Internacional de La Rioja (UNIR)",
        "correo_admin": "@unir.net",
        "correo_estudiantes": "@students.unir.net",
        "programas_intercambio": ["Erasmus+", "Convenios Internacionales"]
      },
      {
        "nombre": "Universidad CEU San Pablo",
        "correo_admin": "@ceu.es",
        "correo_estudiantes": "@alumnos.ceu.es",
        "programas_intercambio": ["Erasmus+", "Red Internacional"]
      },
      {
        "nombre": "Universidad TAI (Artes y Tecnología)",
        "correo_admin": "@tai.edu",
        "correo_estudiantes": "@students.tai.edu",
        "programas_intercambio": ["Programas Propios"]
      },
      {
        "nombre": "Universidad Internacional Villanueva",
        "correo_admin": "@villanueva.edu",
        "correo_estudiantes": "@students.villanueva.edu",
        "programas_intercambio": ["Erasmus+", "Global Exchange"]
      },
      {
        "nombre": "Universidad Europea Miguel de Cervantes (Campus Madrid)",
        "correo_admin": "@uemc.es",
        "correo_estudiantes": "@alumnos.uemc.es",
        "programas_intercambio": ["Convenios Internacionales"]
      },
      {
        "nombre": "EAE Business School (Campus Madrid)",
        "correo_admin": "@eae.es",
        "correo_estudiantes": "@students.eae.es",
        "programas_intercambio": ["Global Mobility"]
      },
      {
        "nombre": "Universidad Internacional SEK",
        "correo_admin": "@sek.edu",
        "correo_estudiantes": "@students.sek.edu",
        "programas_intercambio": ["Erasmus+", "Intercambios Propios"]
      },
      {
        "nombre": "ESIC Business & Marketing School",
        "correo_admin": "@esic.edu",
        "correo_estudiantes": "@students.esic.edu",
        "programas_intercambio": ["Global Exchange", "Erasmus+"]
      }
    ]
    """  // Complete the JSON data

    // Pass JSON to list of University objects
    val universities = Gson().fromJson(universitiesJson, Array<University>::class.java).toList()

    //  add universities to Firestore
    fun addUniversitiesToFirestore() {
        universities.forEach { university ->
            // random UID for each university
            val randomId = UUID.randomUUID().toString()

            dbFirestore.collection("universities")
                .document(randomId) // use random UID as document ID
                .set(university)
                .addOnSuccessListener {
                    Log.d("Firestore", "University ${university.nombre} added successfully!")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error adding university: ${e.message}")
                }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Add Universities to Firestore")
        Button(onClick = { addUniversitiesToFirestore() }) {
            Text("Add Universities")
        }
    }
}
