//package com.ejecicio.myapplication.scenes.authentication
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.selection.selectableGroup
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.compose.ui.platform.LocalContext
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.ejecicio.myapplication.scenes.authentication.User
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//
//
//@Composable
//fun RegisterScreen(navController: NavController) {
//    // Estado para los campos de entrada
//    var name by remember { mutableStateOf("") }
//    var lastname by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }  // Agregamos password
//    var confirmPassword by remember { mutableStateOf("") }  // Confirmación de contraseña
//    var university by remember { mutableStateOf("") }
//    var homeCountry by remember { mutableStateOf("") }
//    var homeCity by remember { mutableStateOf("") }
//
//    // Estado para los hobbies
//    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }
//
//    // Lista de hobbies predefinidos
//    val hobbiesList = listOf("Reading", "Traveling", "Cooking", "Sports", "Music", "Photography")
//
//    // Validación básica del formulario
//    val isFormValid = name.isNotEmpty() && lastname.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty() &&
//            password.isNotEmpty() && confirmPassword == password && university.isNotEmpty() && homeCountry.isNotEmpty() && homeCity.isNotEmpty() && selectedHobbies.isNotEmpty()
//
//    // Acceder al contexto de la aplicación
//    val context = LocalContext.current
//
//    // Crear un ScrollState para permitir el desplazamiento
//    val scrollState = rememberScrollState()
//
//    // Obtener instancias de FirebaseAuth y FirebaseFirestore
//    val auth = FirebaseAuth.getInstance()
//    val dbFirestore = FirebaseFirestore.getInstance()
//
//    // Pantalla de registro
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState) // Habilitar el desplazamiento
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Register", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Campos de texto para el formulario
//        TextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Name") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = lastname,
//            onValueChange = { lastname = it },
//            label = { Text("Last Name") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = age,
//            onValueChange = { age = it },
//            label = { Text("Age") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = { Text("Confirm Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = university,
//            onValueChange = { university = it },
//            label = { Text("University") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = homeCountry,
//            onValueChange = { homeCountry = it },
//            label = { Text("Home Country") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = homeCity,
//            onValueChange = { homeCity = it },
//            label = { Text("Home City") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Selección de hobbies
//        Text("Select your hobbies:", style = MaterialTheme.typography.bodyMedium)
//
//        // Lista de checkboxes para los hobbies
//        Column(modifier = Modifier.selectableGroup()) {
//            hobbiesList.forEach { hobby ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                ) {
//                    Checkbox(
//                        checked = selectedHobbies.contains(hobby),
//                        onCheckedChange = {
//                            selectedHobbies = if (it) {
//                                selectedHobbies + hobby
//                            } else {
//                                selectedHobbies - hobby
//                            }
//                        }
//                    )
//                    Text(hobby)
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Botón de registro
//        Button(
//            onClick = {
//                if (isFormValid) {
//                    // Crear un nuevo usuario con Firebase Authentication
//                    auth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                // Guardar los datos del usuario en Firestore
//                                val user = User(
//                                    uid = auth.currentUser?.uid ?: "",
//                                    name = name,
//                                    lastname = lastname,
//                                    age = age.toInt(),
//                                    email = email,
//                                    university = university,
//                                    homeCountry = homeCountry,
//                                    homeCity = homeCity,
//                                    hobbies = selectedHobbies.toList()
//                                )
//
//                                dbFirestore.collection("users")
//                                    .document(auth.currentUser?.uid ?: "")
//                                    .set(user)
//                                    .addOnSuccessListener {
//                                        Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
//                                        // Navegar a la siguiente pantalla si es necesario
//                                    }
//                                    .addOnFailureListener { e ->
//                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                                    }
//                            } else {
//                                Toast.makeText(context, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                } else {
//                    Toast.makeText(context, "Please fill all fields and ensure passwords match", Toast.LENGTH_SHORT).show()
//                }
//            },
//            enabled = isFormValid,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Register")
//        }
//    }
//}


package com.ejecicio.myapplication.scenes.authentication

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.platform.LocalContext

@Composable
fun RegisterScreen(navController: NavController) {
    // Estado para los campos de entrada
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }  // Agregamos password
    var confirmPassword by remember { mutableStateOf("") }  // Confirmación de contraseña

    // Estado para los hobbies
    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }

    // Lista de hobbies predefinidos
    val hobbiesList = listOf("Reading", "Traveling", "Cooking", "Sports", "Music", "Photography")

    // Validación básica del formulario
    val isFormValid = name.isNotEmpty() && lastname.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty() &&
            password.isNotEmpty() && confirmPassword == password

    // Acceder al contexto de la aplicación
    val context = LocalContext.current

    // Crear un ScrollState para permitir el desplazamiento
    val scrollState = rememberScrollState()

    // Obtener instancias de FirebaseAuth y FirebaseFirestore
    val auth = FirebaseAuth.getInstance()
    val dbFirestore = FirebaseFirestore.getInstance()

    // Pantalla de registro
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Habilitar el desplazamiento
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // Campos de texto para el formulario
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Botón de registro
        Button(
            onClick = {
                if (isFormValid) {
                    // Crear un nuevo usuario con Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Guardar los datos del usuario en Firestore
                                val user = User(
                                    uid = auth.currentUser?.uid ?: "",
                                    name = name,
                                    lastname = lastname,
                                    age = age.toInt(),
                                    email = email,
                                    // Los demás valores serán predeterminados
                                    role = "Student",
                                    university = "",
                                    homeCountry = "",
                                    homeCity = "",
                                    description = "",
                                    hobbies = selectedHobbies.toList()
                                )

                                dbFirestore.collection("users")
                                    .document(auth.currentUser?.uid ?: "")
                                    .set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                                        // Navegar a la siguiente pantalla si es necesario
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please fill all fields and ensure passwords match", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}
