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
import android.app.DatePickerDialog
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var cities by remember { mutableStateOf(listOf<String>()) }
    var selectedCity by remember { mutableStateOf("") }
    var isCityDropdownOpen by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedUniversity by remember { mutableStateOf("") }
    var selectedHobbies by remember { mutableStateOf(setOf<String>()) }
    var emailDomains by remember { mutableStateOf(listOf<String>()) }
    var universities by remember { mutableStateOf(listOf<Pair<String, List<String>>>()) }
    var isUniversityDropdownOpen by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }

    val isCitySelected = selectedCity.isNotEmpty()
    val isUniversitySelected = selectedUniversity.isNotEmpty()
    val isFormValid = name.isNotBlank() && lastname.isNotBlank() &&
            !ageError &&
            email.isNotBlank() && password.isNotBlank() &&
            confirmPassword == password && isCitySelected && isUniversitySelected

    // fetch data
    LaunchedEffect(Unit) {
        db.collection("universities2").get()
            .addOnSuccessListener { snapshot ->
                cities = snapshot.mapNotNull { it.getString("city") }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error loading cities: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    LaunchedEffect(selectedCity) {
        if (selectedCity.isNotEmpty()) {
            db.collection("universities2")
                .whereEqualTo("city", selectedCity)
                .get()
                .addOnSuccessListener { snapshot ->
                    val cityDoc = snapshot.documents.firstOrNull()
                    if (cityDoc != null) {
                        db.collection("universities2")
                            .document(cityDoc.id)
                            .collection("universities")
                            .get()
                            .addOnSuccessListener { subSnapshot ->
                                universities = subSnapshot.mapNotNull { doc ->
                                    val name = doc.getString("name")
                                    val domain = doc.getString("student_email")?.substringAfter('@')
                                    if (name != null && domain != null) Pair(name, listOf(domain)) else null
                                }
                            }
                    }
                }
        } else {
            universities = emptyList()
        }
    }

    MyApplicationTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Register", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(20.dp))

                // city Dropdown (first thing shown, mandatory to choose to be able to complete the other fields)
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

                Spacer(modifier = Modifier.height(20.dp))

                @Composable
                fun customTextField(
                    value: String,
                    label: String,
                    onChange: (String) -> Unit,
                    keyboardType: KeyboardType = KeyboardType.Text,
                    isPassword: Boolean = false,
                    enabled: Boolean = true,
                    isError: Boolean = false
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onChange,
                        label = { Text(label) },
                        singleLine = true,
                        enabled = enabled,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        isError = isError,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Rest of form (it now workd), shown only if city selected
                customTextField(name, "Name", { name = it }, enabled = isCitySelected)
                Spacer(modifier = Modifier.height(10.dp))

                customTextField(lastname, "Last Name", { lastname = it }, enabled = isCitySelected)
                Spacer(modifier = Modifier.height(10.dp))

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val context = LocalContext.current

                DatePickerField(
                    date = age,
                    onDateSelected = { selectedDate ->
                        age = selectedDate
                        val birthYear = selectedDate.takeLast(4).toIntOrNull()
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                        val userAge = if (birthYear != null) currentYear - birthYear else 0
                        ageError = userAge !in 17..95
                    },
                    isEnabled = isCitySelected,
                    isError = ageError
                )

                if (ageError) {
                    Text("You must be between 17 and 95 years old", color = MaterialTheme.colorScheme.error)
                }



                Spacer(modifier = Modifier.height(10.dp))

                // University Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = isCitySelected) { isUniversityDropdownOpen = true }
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
                        universities.forEach { (uni, domains) ->
                            DropdownMenuItem(
                                text = { Text(uni) },
                                onClick = {
                                    selectedUniversity = uni
                                    emailDomains = domains
                                    isUniversityDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

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
                                            age = age,
                                            email = email,
                                            university = selectedUniversity,
                                            city = selectedCity,
                                            role = "Student",
                                            homeCountry = "",
                                            homeCity = "",
                                            description = "",
                                            hobbies = selectedHobbies.toList()
                                        )
                                        db.collection("users")
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

@Composable
fun DatePickerField(
    date: String,
    onDateSelected: (String) -> Unit,
    isEnabled: Boolean,
    isError: Boolean
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                val userAge = year - selectedYear
                onDateSelected(formattedDate)
                showDatePicker = false
            },
            year,
            month,
            day
        ).show()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) { showDatePicker = true }
            .border(1.dp, if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = if (date.isBlank()) "Select Birthdate" else date,
            color = if (date.isBlank()) Color.Gray else MaterialTheme.colorScheme.onSurface
        )
    }
}


