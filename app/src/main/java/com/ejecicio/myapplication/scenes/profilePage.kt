package com.ejecicio.myapplication.scenes

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
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
    var hobbies by remember { mutableStateOf(setOf<String>()) }

    var isDarkMode by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    MyApplicationTheme(isDarkMode = isDarkMode) {
        val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )

        LaunchedEffect(userId) {
            currentUser?.uid?.let { uid ->
                dbFirestore.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        name = document.getString("name") ?: ""
                        lastname = document.getString("lastname") ?: ""
                        age = document.getLong("age")?.toString() ?: ""
                        university = document.getString("university") ?: ""
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(24.dp))

//                    @Composable
//                    fun profileField(value: String, onValueChange: (String) -> Unit, label: String, enabled: Boolean = true, keyboardType: KeyboardType = KeyboardType.Text) {
//                        OutlinedTextField(
//                            value = value,
//                            onValueChange = onValueChange,
//                            label = { Text(label) },
//                            enabled = enabled,
//                            shape = RoundedCornerShape(8.dp),
//                            colors = textFieldColors, // Use dynamic colors here
//                            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//                            textStyle = TextStyle(
//                                color = MaterialTheme.colorScheme.onSurface // Text color will now adapt based on the theme
//                            ),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp)
//                        )
//                    }
@Composable
fun profileField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val textColor = if (enabled) {
        MaterialTheme.colorScheme.onSurface // or any custom color for enabled
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // dimmed for disabled
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(color = textColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}



                    profileField(name, { name = it }, "Name")
                    profileField(lastname, { lastname = it }, "Last Name")
                    profileField(age, { age = it }, "Age", keyboardType = KeyboardType.Number)
                    profileField(email, {}, "Email", enabled = false)
                    profileField(university, {}, "University", enabled = false)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val updatedUser = mapOf(
                                "name" to name,
                                "lastname" to lastname,
                                "age" to (age.toIntOrNull() ?: 0),
                                "hobbies" to hobbies.toList()
                            )
                            currentUser?.uid?.let { uid ->
                                dbFirestore.collection("users").document(uid).update(updatedUser)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text("Save Changes")
                    }

                    TextButton(
                        onClick = { isDarkMode = !isDarkMode },
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text("Toggle Light/Dark Mode", color = MaterialTheme.colorScheme.primary)
                    }
                }

                FloatingBottomNavBar(
                    navController = navController,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }}
