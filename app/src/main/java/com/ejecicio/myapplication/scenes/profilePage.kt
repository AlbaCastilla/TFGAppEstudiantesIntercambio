package com.ejecicio.myapplication.scenes

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar
import com.ejecicio.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("userId", null) ?: ""

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val dbFirestore = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var university by remember { mutableStateOf("") }
    var homeCountry by remember { mutableStateOf("") }
    var homeCity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hobbies by remember { mutableStateOf(setOf<String>()) }

    var isDarkMode by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        currentUser?.uid?.let { uid ->
            dbFirestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    name = document.getString("name") ?: ""
                    lastname = document.getString("lastname") ?: ""
                    age = document.getLong("age")?.toString() ?: ""
                    university = document.getString("university") ?: ""
                    homeCountry = document.getString("homeCountry") ?: ""
                    homeCity = document.getString("homeCity") ?: ""
                    description = document.getString("description") ?: ""
                    hobbies = document.get("hobbies") as? Set<String> ?: emptySet()
                    isLoading = false
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
        }
    }

    MyApplicationTheme(isDarkMode = isDarkMode) {
        val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Añadimos verticalScroll aquí
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Light/Dark Mode Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isDarkMode) "Dark Mode" else "Light Mode",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.LightMode,
                        contentDescription = "Light Mode",
                        tint = if (!isDarkMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { isDarkMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = "Dark Mode",
                        tint = if (!isDarkMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                @Composable
                fun profileField(
                    value: String,
                    onValueChange: (String) -> Unit,
                    label: String,
                    enabled: Boolean = true
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        label = { Text(label) },
                        enabled = enabled,
                        shape = RoundedCornerShape(8.dp),
                        colors = textFieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                profileField(name, { name = it }, "Name")
                profileField(lastname, { lastname = it }, "Last Name")
                profileField(age, { age = it }, "Age")
                profileField(email, {}, "Email", enabled = false)
                profileField(university, {}, "University", enabled = false)
                profileField(homeCountry, { homeCountry = it }, "Home Country")
                profileField(homeCity, { homeCity = it }, "Home City")
                profileField(description, { description = it }, "Description")

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val updatedUser = mapOf(
                            "name" to name,
                            "lastname" to lastname,
                            "age" to (age.toIntOrNull() ?: 0),
                            "homeCountry" to homeCountry,
                            "homeCity" to homeCity,
                            "description" to description,
                            "hobbies" to hobbies.toList()
                        )
                        currentUser?.uid?.let { uid ->
                            dbFirestore.collection("users").document(uid).update(updatedUser)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Profile Updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Save Changes")
                }

                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = 70.dp)
                ) {
                    Text(
                        text = "Log Out",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .background(Color.Red, RoundedCornerShape(8.dp))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }

            FloatingBottomNavBar(
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}