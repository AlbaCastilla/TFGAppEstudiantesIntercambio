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

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.ejecicio.myapplication.ui.theme.MyApplicationTheme

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }
    var universities by remember { mutableStateOf(listOf<Pair<String, List<String>>>()) }
    var selectedUniversity by remember { mutableStateOf("") }
    var emailDomains by remember { mutableStateOf(listOf<String>()) }
    var isDropdownOpen by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val auth = FirebaseAuth.getInstance()
    val dbFirestore = FirebaseFirestore.getInstance()

    val isUniversitySelected = selectedUniversity.isNotEmpty()
//    val isFormValid = name.isNotEmpty() && lastname.isNotEmpty() && age.isNotEmpty() &&
//            email.isNotEmpty() && password.isNotEmpty() && confirmPassword == password &&
//            selectedUniversity.isNotEmpty()
    val isFormValid = name.trim().isNotEmpty() &&
            lastname.trim().isNotEmpty() &&
            age.toIntOrNull()?.let { it in 17..95 } == true &&
            email.trim().isNotEmpty() &&
            password.isNotEmpty() &&
            confirmPassword == password &&
            selectedUniversity.trim().isNotEmpty()


    LaunchedEffect(Unit) {
        dbFirestore.collection("universities").get()
            .addOnSuccessListener { snapshot ->
                universities = snapshot.mapNotNull { doc ->
                    val name = doc.getString("nombre")
                    val emailDomain = doc.getString("correo_estudiantes")?.substringAfter('@')
                    if (name != null && emailDomain != null) Pair(name, listOf(emailDomain)) else null
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    LaunchedEffect(name, lastname, age, email, password, confirmPassword, selectedUniversity) {
        Log.d("DEBUG", "name=$name lastname=$lastname age=$age email=$email password=$password confirm=$confirmPassword selectedUni=$selectedUniversity isFormValid=$isFormValid")
    }


    MyApplicationTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Register", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(20.dp))

                @Composable
                fun customTextField(value: String, label: String, onChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, isPassword: Boolean = false, enabled: Boolean = true, isError: Boolean = false) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onChange,
                        label = { Text(label) },
                        singleLine = true,
                        enabled = enabled,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        isError = isError,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                customTextField(name, "Name", { name = it })
                Spacer(modifier = Modifier.height(10.dp))

                customTextField(lastname, "Last Name", { lastname = it })
                Spacer(modifier = Modifier.height(10.dp))

                customTextField(age, "Age", {
                    age = it
                    ageError = it.toIntOrNull()?.let { it !in 17..95 } ?: false
                }, keyboardType = KeyboardType.Number, isError = ageError)

                if (ageError) {
                    Text("Age must be between 17 and 95", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(18.dp))
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDropdownOpen = true }
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedUniversity.isEmpty()) "Select University" else selectedUniversity,
                            modifier = Modifier.weight(1f),
                            color = if (selectedUniversity.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }

//                    DropdownMenu(expanded = isDropdownOpen, onDismissRequest = { isDropdownOpen = false }) {
//                        universities.forEach { (uni, domains) ->
//                            DropdownMenuItem(
//                                text = { Text(uni) },
//                                onClick = {
//                                    selectedUniversity = uni
//                                    emailDomains = domains
//                                    isDropdownOpen = false
//                                }
//                            )
//                        }
//                    }
                    DropdownMenu(
                        expanded = isDropdownOpen,
                        onDismissRequest = { isDropdownOpen = false },
                        modifier = Modifier
                            .fillMaxWidth(0.90f)
                            .heightIn(max = 200.dp)
                    ) {
                        universities.forEach { (uni, domains) ->
                            DropdownMenuItem(
                                text = { Text(uni, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                onClick = {
                                    selectedUniversity = uni
                                    emailDomains = domains
                                    isDropdownOpen = false
                                }
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))

                customTextField(email, "Email", { email = it }, KeyboardType.Email, enabled = isUniversitySelected)
                Spacer(modifier = Modifier.height(10.dp))

                customTextField(password, "Password", { password = it }, KeyboardType.Password, isPassword = true, enabled = isUniversitySelected)
                Spacer(modifier = Modifier.height(10.dp))

                customTextField(confirmPassword, "Confirm Password", { confirmPassword = it }, KeyboardType.Password, isPassword = true, enabled = isUniversitySelected)
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (isFormValid) {
                            val emailDomain = email.substringAfter('@')
                            if (emailDomains.contains(emailDomain)) {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnSuccessListener {
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
                                                Toast.makeText(context, "User Registered", Toast.LENGTH_SHORT).show()
                                                navController.navigate("login") {
                                                    popUpTo("RegisterScreen") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Firestore error: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Auth failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Invalid email domain", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Fill in all fields and make sure passwords match", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Register")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Already have an account? Login",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("login")
                        }
                        .padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

