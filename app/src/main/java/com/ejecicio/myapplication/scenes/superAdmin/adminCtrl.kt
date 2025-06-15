package com.ejecicio.myapplication.scenes.superAdmin

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.scenes.authentication.DatePickerField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar


@Composable
fun AdminCtrl(navController: NavHostController) {
    var selectedRole by remember { mutableStateOf("Student") }
    var isRoleDropdownExpanded by remember { mutableStateOf(false) }
    val roles = listOf("Student", "Admin", "Super Admin")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User Registration", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))

        // role dropdown - mandatory to choose to be able to complete the other fields
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                label = { Text("Select Role") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isRoleDropdownExpanded = true },
                enabled = false
            )
            DropdownMenu(
                expanded = isRoleDropdownExpanded,
                onDismissRequest = { isRoleDropdownExpanded = false }
            ) {
                roles.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selectedRole = it
                            isRoleDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        // show the correct form based on selected  user role
        when (selectedRole) {
            "Student" -> StudentForm(navController, selectedRole, auth, db)
            "Admin" -> AdminForm(navController, selectedRole, auth, db)
            "Super Admin" -> SuperAdminForm(navController, selectedRole, auth, db)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentForm(navController: NavHostController, selectedRole: String, auth: FirebaseAuth, db: FirebaseFirestore) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()


    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var cities by remember { mutableStateOf(listOf<String>()) }
    var isCityDropdownOpen by remember { mutableStateOf(false) }
    var universities by remember { mutableStateOf(listOf<Pair<String, List<String>>>()) }
    var selectedUniversity by remember { mutableStateOf("") }
    var isUniversityDropdownOpen by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }
    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }

    val isFormValid = name.isNotBlank() && lastname.isNotBlank() &&
            age.isNotBlank() && !ageError &&
            email.isNotBlank() && password.isNotBlank() &&
            confirmPassword == password &&
            selectedCity.isNotBlank() && selectedUniversity.isNotBlank()

    // cities
    LaunchedEffect(Unit) {
        db.collection("universities2").get()
            .addOnSuccessListener { snapshot ->
                cities = snapshot.mapNotNull { it.getString("city") }
            }
    }

    // universities based on city selected
    LaunchedEffect(selectedCity) {
        if (selectedCity.isNotEmpty()) {
            db.collection("universities2")
                .whereEqualTo("city", selectedCity)
                .get()
                .addOnSuccessListener { snapshot ->
                    val doc = snapshot.documents.firstOrNull()
                    doc?.let {
                        db.collection("universities2")
                            .document(it.id)
                            .collection("universities")
                            .get()
                            .addOnSuccessListener { subSnap ->
                                universities = subSnap.mapNotNull { uni ->
                                    val name = uni.getString("name")
                                    val domain = uni.getString("student_email")?.substringAfter('@')
                                    if (name != null && domain != null) Pair(name, listOf(domain)) else null
                                }
                            }
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register Student", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        // city dropdown
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isCityDropdownOpen = true }
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCity.isEmpty()) "Select City" else selectedCity,
                    modifier = Modifier.weight(1f),
                    color = if (selectedCity.isEmpty()) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = isCityDropdownOpen,
                onDismissRequest = { isCityDropdownOpen = false }
            ) {
                cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city) },
                        onClick = {
                            selectedCity = city
                            isCityDropdownOpen = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // all fields disabled if no city selected
        val enabled = selectedCity.isNotEmpty()

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(10.dp))

        DatePickerField(
            date = age,
            onDateSelected = {
                age = it
                val birthYear = it.takeLast(4).toIntOrNull()
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val calculatedAge = if (birthYear != null) currentYear - birthYear else 0
                ageError = calculatedAge !in 17..95
            },
            isEnabled = enabled,
            isError = ageError
        )
        if (ageError) {
            Text("Age must be between 17 and 95", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { isUniversityDropdownOpen = true }
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedUniversity.isEmpty()) "Select University" else selectedUniversity,
                    modifier = Modifier.weight(1f),
                    color = if (selectedUniversity.isEmpty()) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = isUniversityDropdownOpen,
                onDismissRequest = { isUniversityDropdownOpen = false }
            ) {
                universities.forEach { (name, _) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedUniversity = name
                            isUniversityDropdownOpen = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (!isFormValid) {
                    Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val userId = result.user?.uid ?: return@addOnSuccessListener
                        val studentData = hashMapOf(
                            "uid" to userId,
                            "name" to name,
                            "lastname" to lastname,
                            "birthdate" to age,
                            "email" to email,
                            "role" to selectedRole,
                            "city" to selectedCity,
                            "university" to selectedUniversity,
                            "homeCountry" to "",
                            "homeCity" to "",
                            "description" to "",
                            "hobbies" to selectedHobbies.toList()
                        )

                        db.collection("users").document(userId)
                            .set(studentData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Student Registered", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Firestore error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Auth error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }

            },
            enabled = isFormValid && enabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register Student")
        }

    }
}


@Composable
fun AdminForm(navController: NavHostController, selectedRole: String, auth: FirebaseAuth, db: FirebaseFirestore)
 {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var selectedCity by remember { mutableStateOf("") }
    var newCity by remember { mutableStateOf("") }
    var cities by remember { mutableStateOf(listOf<String>()) }
    var isCityDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            password == confirmPassword &&
            (selectedCity.isNotBlank() || newCity.isNotBlank())

    // load city names
    LaunchedEffect(Unit) {
        db.collection("universities2").get().addOnSuccessListener { documents ->
            val cityList = documents.mapNotNull { it.getString("city") }
            cities = cityList.distinct()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Admin Registration", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // city dropdownm
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = if (selectedCity.isNotBlank()) selectedCity else newCity,
                onValueChange = {},
                label = { Text("City") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isCityDropdownExpanded = true },
                enabled = false
            )

            DropdownMenu(
                expanded = isCityDropdownExpanded,
                onDismissRequest = { isCityDropdownExpanded = false }
            ) {
                cities.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selectedCity = it
                            newCity = ""
                            isCityDropdownExpanded = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("Add New City") },
                    onClick = {
                        selectedCity = ""
                        isCityDropdownExpanded = false
                    }
                )
            }
        }

        if (selectedCity.isBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = newCity,
                onValueChange = { newCity = it },
                label = { Text("New City Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val cityToSave = if (selectedCity.isNotBlank()) selectedCity else newCity.trim()

                if (!isFormValid) {
                    Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Add city if it's new
                if (selectedCity.isBlank()) {
                    db.collection("universities2").add(mapOf("city" to cityToSave))
                }

                // create admin user (default role = "admin")
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: return@addOnSuccessListener
                        val user = mapOf(
                            "name" to name,
                            "email" to email,
                            "role" to "admin",
                            "uid" to uid,
                            "city" to cityToSave
                        )
                        db.collection("users").document(uid).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Admin registered", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to save user: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register Admin")
        }
    }
}
@Composable
fun SuperAdminForm(
    navController: NavHostController,
    selectedRole: String,
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            password == confirmPassword

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Super Admin Registration", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (isFormValid) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: return@addOnSuccessListener
                            val user = mapOf(
                                "name" to name,
                                "email" to email,
                                "role" to "super_admin",
                                "uid" to uid
                            )
                            db.collection("users").document(uid).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Super Admin registered", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to save user: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register Super Admin")
        }
    }
}