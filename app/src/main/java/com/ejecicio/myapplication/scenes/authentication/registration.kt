////package com.ejecicio.myapplication.scenes.authentication
////
////import android.widget.Toast
////import androidx.compose.foundation.background
////import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.selection.selectableGroup
////import androidx.compose.foundation.verticalScroll
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.unit.dp
////import androidx.navigation.NavController
////import androidx.compose.ui.platform.LocalContext
////import com.google.firebase.auth.FirebaseAuth
////import com.google.firebase.firestore.FirebaseFirestore
////import com.ejecicio.myapplication.scenes.authentication.User
////import androidx.compose.foundation.rememberScrollState
////import androidx.compose.foundation.text.KeyboardOptions
////import androidx.compose.ui.text.input.KeyboardType
////import androidx.compose.ui.text.input.PasswordVisualTransformation
////
////
////@Composable
////fun RegisterScreen(navController: NavController) {
////    // Estado para los campos de entrada
////    var name by remember { mutableStateOf("") }
////    var lastname by remember { mutableStateOf("") }
////    var age by remember { mutableStateOf("") }
////    var email by remember { mutableStateOf("") }
////    var password by remember { mutableStateOf("") }  // Agregamos password
////    var confirmPassword by remember { mutableStateOf("") }  // Confirmación de contraseña
////    var university by remember { mutableStateOf("") }
////    var homeCountry by remember { mutableStateOf("") }
////    var homeCity by remember { mutableStateOf("") }
////
////    // Estado para los hobbies
////    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }
////
////    // Lista de hobbies predefinidos
////    val hobbiesList = listOf("Reading", "Traveling", "Cooking", "Sports", "Music", "Photography")
////
////    // Validación básica del formulario
////    val isFormValid = name.isNotEmpty() && lastname.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty() &&
////            password.isNotEmpty() && confirmPassword == password && university.isNotEmpty() && homeCountry.isNotEmpty() && homeCity.isNotEmpty() && selectedHobbies.isNotEmpty()
////
////    // Acceder al contexto de la aplicación
////    val context = LocalContext.current
////
////    // Crear un ScrollState para permitir el desplazamiento
////    val scrollState = rememberScrollState()
////
////    // Obtener instancias de FirebaseAuth y FirebaseFirestore
////    val auth = FirebaseAuth.getInstance()
////    val dbFirestore = FirebaseFirestore.getInstance()
////
////    // Pantalla de registro
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .verticalScroll(scrollState) // Habilitar el desplazamiento
////            .padding(16.dp),
////        horizontalAlignment = Alignment.CenterHorizontally
////    ) {
////        Text("Register", style = MaterialTheme.typography.headlineMedium)
////
////        Spacer(modifier = Modifier.height(20.dp))
////
////        // Campos de texto para el formulario
////        TextField(
////            value = name,
////            onValueChange = { name = it },
////            label = { Text("Name") },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = lastname,
////            onValueChange = { lastname = it },
////            label = { Text("Last Name") },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = age,
////            onValueChange = { age = it },
////            label = { Text("Age") },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true,
////            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = email,
////            onValueChange = { email = it },
////            label = { Text("Email") },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = password,
////            onValueChange = { password = it },
////            label = { Text("Password") },
////            modifier = Modifier.fillMaxWidth(),
////            visualTransformation = PasswordVisualTransformation(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = confirmPassword,
////            onValueChange = { confirmPassword = it },
////            label = { Text("Confirm Password") },
////            modifier = Modifier.fillMaxWidth(),
////            visualTransformation = PasswordVisualTransformation(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = university,
////            onValueChange = { university = it },
////            label = { Text("University") },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = homeCountry,
////            onValueChange = { homeCountry = it },
////            label = { Text("Home Country") },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(10.dp))
////
////        TextField(
////            value = homeCity,
////            onValueChange = { homeCity = it },
////            label = { Text("Home City") },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true
////        )
////        Spacer(modifier = Modifier.height(20.dp))
////
////        // Selección de hobbies
////        Text("Select your hobbies:", style = MaterialTheme.typography.bodyMedium)
////
////        // Lista de checkboxes para los hobbies
////        Column(modifier = Modifier.selectableGroup()) {
////            hobbiesList.forEach { hobby ->
////                Row(
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(8.dp)
////                ) {
////                    Checkbox(
////                        checked = selectedHobbies.contains(hobby),
////                        onCheckedChange = {
////                            selectedHobbies = if (it) {
////                                selectedHobbies + hobby
////                            } else {
////                                selectedHobbies - hobby
////                            }
////                        }
////                    )
////                    Text(hobby)
////                }
////            }
////        }
////        Spacer(modifier = Modifier.height(20.dp))
////
////        // Botón de registro
////        Button(
////            onClick = {
////                if (isFormValid) {
////                    // Crear un nuevo usuario con Firebase Authentication
////                    auth.createUserWithEmailAndPassword(email, password)
////                        .addOnCompleteListener { task ->
////                            if (task.isSuccessful) {
////                                // Guardar los datos del usuario en Firestore
////                                val user = User(
////                                    uid = auth.currentUser?.uid ?: "",
////                                    name = name,
////                                    lastname = lastname,
////                                    age = age.toInt(),
////                                    email = email,
////                                    university = university,
////                                    homeCountry = homeCountry,
////                                    homeCity = homeCity,
////                                    hobbies = selectedHobbies.toList()
////                                )
////
////                                dbFirestore.collection("users")
////                                    .document(auth.currentUser?.uid ?: "")
////                                    .set(user)
////                                    .addOnSuccessListener {
////                                        Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
////                                        // Navegar a la siguiente pantalla si es necesario
////                                    }
////                                    .addOnFailureListener { e ->
////                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
////                                    }
////                            } else {
////                                Toast.makeText(context, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
////                            }
////                        }
////                } else {
////                    Toast.makeText(context, "Please fill all fields and ensure passwords match", Toast.LENGTH_SHORT).show()
////                }
////            },
////            enabled = isFormValid,
////            modifier = Modifier.fillMaxWidth()
////        ) {
////            Text("Register")
////        }
////    }
////}
//
//
package com.ejecicio.myapplication.scenes.authentication

import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.platform.LocalContext

//@Composable
//fun RegisterScreen(navController: NavController) {
//    // Estado para los campos de entrada
//    var name by remember { mutableStateOf("") }
//    var lastname by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }  // Agregamos password
//    var confirmPassword by remember { mutableStateOf("") }  // Confirmación de contraseña
//
//    // Estado para los hobbies
//    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }
//
//    // Lista de hobbies predefinidos
//    val hobbiesList = listOf("Reading", "Traveling", "Cooking", "Sports", "Music", "Photography")
//
//    // Validación básica del formulario
//    val isFormValid = name.isNotEmpty() && lastname.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty() &&
//            password.isNotEmpty() && confirmPassword == password
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
//                                    // Los demás valores serán predeterminados
//                                    role = "Student",
//                                    university = "",
//                                    homeCountry = "",
//                                    homeCity = "",
//                                    description = "",
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

//@Composable
//fun RegisterScreen(navController: NavController) {
//    // Estado para los campos de entrada
//    var name by remember { mutableStateOf("") }
//    var lastname by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//
//    // Estado para los hobbies
//    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }
//
//    // Estado para las universidades y la universidad seleccionada
//    var universities by remember { mutableStateOf(listOf<String>()) }
//    var selectedUniversity by remember { mutableStateOf("") }
//    var isDropdownOpen by remember { mutableStateOf(false) } // Estado para manejar el menú desplegable
//
//    // Validación básica del formulario
//    val isFormValid = name.isNotEmpty() && lastname.isNotEmpty() && age.isNotEmpty() &&
//            email.isNotEmpty() && password.isNotEmpty() && confirmPassword == password &&
//            selectedUniversity.isNotEmpty()
//
//    val context = LocalContext.current
//    val scrollState = rememberScrollState()
//
//    val auth = FirebaseAuth.getInstance()
//    val dbFirestore = FirebaseFirestore.getInstance()
//
//    val emailDomains = remember { mutableStateListOf<String>() }
//
//    // Cargar las terminaciones de correos y las universidades desde Firestore
//    LaunchedEffect(Unit) {
//        dbFirestore.collection("universities")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val fetchedUniversities = mutableListOf<String>()
//                for (document in querySnapshot) {
//                    val correoAdmin = document.getString("correo_admin")
//                    val correoEstudiantes = document.getString("correo_estudiantes")
//                    val universityName = document.getString("nombre")
//
//                    correoAdmin?.substringAfter('@')?.let { emailDomains.add(it) }
//                    correoEstudiantes?.substringAfter('@')?.let { emailDomains.add(it) }
//                    universityName?.let { fetchedUniversities.add(it) }
//                }
//                universities = fetchedUniversities
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    // Pantalla de registro
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState)
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Register", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Campos de texto para el formulario
//        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(value = lastname, onValueChange = { lastname = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
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
//        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
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
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Dropdown para seleccionar universidad
//        Text("Select your University", style = MaterialTheme.typography.bodyMedium)
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Box {
//            // Trigger para abrir el menú desplegable
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { isDropdownOpen = true }
//                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
//                    .padding(12.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = if (selectedUniversity.isEmpty()) "Select University" else selectedUniversity,
//                    modifier = Modifier.weight(1f),
//                    color = if (selectedUniversity.isEmpty()) Color.Gray else Color.Black
//                )
//                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
//            }
//
//            DropdownMenu(
//                expanded = isDropdownOpen,
//                onDismissRequest = { isDropdownOpen = false }
//            ) {
//                universities.forEach { university ->
//                    DropdownMenuItem(
//                        text = { Text(university) },
//                        onClick = {
//                            selectedUniversity = university
//                            isDropdownOpen = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Botón de registro
//        Button(
//            onClick = {
//                if (isFormValid) {
//                    val emailDomain = email.substringAfter('@')
//                    if (emailDomains.contains(emailDomain)) {
//                        auth.createUserWithEmailAndPassword(email, password)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    val user = User(
//                                        uid = auth.currentUser?.uid ?: "",
//                                        name = name,
//                                        lastname = lastname,
//                                        age = age.toInt(),
//                                        email = email,
//                                        university = selectedUniversity,
//                                        role = "Student",
//                                        homeCountry = "",
//                                        homeCity = "",
//                                        description = "",
//                                        hobbies = selectedHobbies.toList()
//                                    )
//                                    dbFirestore.collection("users")
//                                        .document(auth.currentUser?.uid ?: "")
//                                        .set(user)
//                                        .addOnSuccessListener {
//                                            Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
//                                        }
//                                        .addOnFailureListener { e ->
//                                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                                        }
//                                } else {
//                                    Toast.makeText(context, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                    } else {
//                        Toast.makeText(context, "Invalid email domain", Toast.LENGTH_SHORT).show()
//                    }
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


@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }

    var universities by remember { mutableStateOf(listOf<Pair<String, List<String>>>()) } // Pair of University Name and Email Endings
    var selectedUniversity by remember { mutableStateOf("") }
    var emailDomains by remember { mutableStateOf(listOf<String>()) } // Filtered domains for the selected university
    var isDropdownOpen by remember { mutableStateOf(false) }

    val isUniversitySelected = selectedUniversity.isNotEmpty()
    val isFormValid = name.isNotEmpty() && lastname.isNotEmpty() && age.isNotEmpty() &&
            email.isNotEmpty() && password.isNotEmpty() && confirmPassword == password &&
            selectedUniversity.isNotEmpty()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val auth = FirebaseAuth.getInstance()
    val dbFirestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        dbFirestore.collection("universities")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val fetchedUniversities = mutableListOf<Pair<String, List<String>>>()
                for (document in querySnapshot) {
                    val universityName = document.getString("nombre")
                    val emailEndings = mutableListOf<String>()

                    //document.getString("correo_admin")?.substringAfter('@')?.let { emailEndings.add(it) }
                    document.getString("correo_estudiantes")?.substringAfter('@')?.let { emailEndings.add(it) }
                    /// AHORA MISMO SOLO HABILITADO PARA ESTUDIANTES


                    if (universityName != null) {
                        fetchedUniversities.add(Pair(universityName, emailEndings))
                    }
                }
                universities = fetchedUniversities
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(10.dp))

        TextField(value = lastname, onValueChange = { lastname = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(10.dp))

        var ageError by remember { mutableStateOf(false) }
        TextField(
            value = age,
            onValueChange = {
                age = it
                ageError = it.toIntOrNull()?.let { it !in 17..95 } ?: false
            },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = ageError // Muestra error visual si es necesario
        )

        if (ageError) {
            Text(
                text = "Age must be between 17 and 95",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        // Dropdown for University Selection
        Text("Select your University", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(10.dp))

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDropdownOpen = true }
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedUniversity.isEmpty()) "Select University" else selectedUniversity,
                    modifier = Modifier.weight(1f),
                    color = if (selectedUniversity.isEmpty()) Color.Gray else Color.Black
                )
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
            }

            DropdownMenu(
                expanded = isDropdownOpen,
                onDismissRequest = { isDropdownOpen = false }
            ) {
                universities.forEach { university ->
                    DropdownMenuItem(
                        text = { Text(university.first) },
                        onClick = {
                            selectedUniversity = university.first
                            emailDomains = university.second // Update email domains for the selected university
                            isDropdownOpen = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Email and Password Fields (Disabled until a university is selected)
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = isUniversitySelected
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = isUniversitySelected
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = isUniversitySelected
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (isFormValid) {
                    val emailDomain = email.substringAfter('@')
                    if (emailDomains.contains(emailDomain)) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = User(
                                        uid = auth.currentUser?.uid ?: "",
                                        name = name,
                                        lastname = lastname,
                                        age = age.toIntOrNull() ?: 0,
                                        email = email,
                                        university = selectedUniversity,
                                        role = "Student",
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

                                            // Navigate to ActivityPage
                                            navController.navigate("login") {
                                                popUpTo("RegisterScreen") { inclusive = true }
                                            }

                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }

                                } else {
                                    Toast.makeText(context, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Invalid email domain", Toast.LENGTH_SHORT).show()
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
