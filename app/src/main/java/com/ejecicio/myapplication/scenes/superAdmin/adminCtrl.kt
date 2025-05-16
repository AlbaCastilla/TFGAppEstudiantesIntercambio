//package com.ejecicio.myapplication.scenes.superAdmin
//
//import android.widget.Toast
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material3.Button
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//@Composable
//fun AdminCtrl(navController: NavHostController) {
//    val auth = FirebaseAuth.getInstance()
//    val db = FirebaseFirestore.getInstance()
//    val context = LocalContext.current
//
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var role by remember { mutableStateOf("Student") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var isDropdownExpanded by remember { mutableStateOf(false) }
//
//    val roles = listOf("Student", "Admin", "Super Admin")
//    val isFormValid = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && password == confirmPassword
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp)
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Simple Registration", style = MaterialTheme.typography.headlineSmall)
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Box(modifier = Modifier.fillMaxWidth()) {
//            OutlinedTextField(
//                value = role,
//                onValueChange = {},
//                label = { Text("Role") },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { isDropdownExpanded = true },
//                enabled = false // Disable direct typing
//            )
//            DropdownMenu(
//                expanded = isDropdownExpanded,
//                onDismissRequest = { isDropdownExpanded = false }
//            ) {
//                roles.forEach {
//                    DropdownMenuItem(
//                        text = { Text(it) },
//                        onClick = {
//                            role = it
//                            isDropdownExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        OutlinedTextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = { Text("Confirm Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(
//            onClick = {
//                if (isFormValid) {
//                    auth.createUserWithEmailAndPassword(email, password)
//                        .addOnSuccessListener { result ->
//                            val userId = result.user?.uid ?: return@addOnSuccessListener
//                            val uid = result.user?.uid
//                            val user = mapOf(
//                                "name" to name,
//                                "email" to email,
//                                "role" to role,
//                                "uid" to uid
//                            )
//                            db.collection("users").document(userId).set(user)
//                                .addOnSuccessListener {
//                                    Toast.makeText(context, "User registered", Toast.LENGTH_SHORT).show()
//                                }
//                                .addOnFailureListener {
//                                    Toast.makeText(context, "Failed to save user: ${it.message}", Toast.LENGTH_SHORT).show()
//                                }
//                        }
//                        .addOnFailureListener {
//                            Toast.makeText(context, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
//                        }
//                } else {
//                    Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Register")
//        }
//    }
//}

package com.ejecicio.myapplication.scenes.superAdmin

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminCtrl(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Student") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var newCity by remember { mutableStateOf("") }

    var roles = listOf("Student", "Admin", "Super Admin")
    var isRoleDropdownExpanded by remember { mutableStateOf(false) }

    var cities by remember { mutableStateOf(listOf<String>()) }
    var isCityDropdownExpanded by remember { mutableStateOf(false) }

    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            password == confirmPassword &&
            (selectedCity.isNotBlank() || newCity.isNotBlank())

    // Load city names from "universities2"
    LaunchedEffect(true) {
        db.collection("universities2").get().addOnSuccessListener { documents ->
            val cityList = documents.mapNotNull { it.getString("city") }
            cities = cityList.distinct()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Simple Registration", style = MaterialTheme.typography.headlineSmall)
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

        // Role Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = role,
                onValueChange = {},
                label = { Text("Role") },
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
                            role = it
                            isRoleDropdownExpanded = false
                        }
                    )
                }
            }
        }

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

        // City Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCity,
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

                // Add new city to universities2 if needed
                if (selectedCity.isBlank()) {
                    db.collection("universities2").add(mapOf("city" to cityToSave))
                }

                // Register user
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: return@addOnSuccessListener
                        val user = mapOf(
                            "name" to name,
                            "email" to email,
                            "role" to role,
                            "uid" to uid,
                            "city" to cityToSave
                        )
                        db.collection("users").document(uid).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(context, "User registered", Toast.LENGTH_SHORT).show()
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
            Text("Register")
        }
    }
}
