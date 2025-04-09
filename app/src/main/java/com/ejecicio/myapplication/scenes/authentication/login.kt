package com.ejecicio.myapplication.scenes.authentication

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoggingIn by remember { mutableStateOf(false) } // To indicate login progress
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        // Email Input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Password Input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Login Button
        Button(
            onClick = {
                isLoggingIn = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        isLoggingIn = false
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                // Fetch user name from Firestore
                                db.collection("users").document(userId).get()
                                    .addOnSuccessListener { document ->
                                        val userName = document.getString("name") ?: "Unknown"
                                        with(sharedPreferences.edit()) {
                                            putString("userId", userId)
                                            putString("userName", userName)
                                            apply()
                                        }

                                        // Navigate to ActivityPage
                                        navController.navigate("activityPage") {
                                            popUpTo("LoginScreen") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Failed to fetch user name", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoggingIn
        ) {
            if (isLoggingIn) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Logging In...")
            } else {
                Text("Login")
            }
        }
    }
}

//fun LoginScreen(navController: NavController) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isLoggingIn by remember { mutableStateOf(false) } // To indicate login progress
//    val context = LocalContext.current
//    val auth = FirebaseAuth.getInstance()
//
//    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Login", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Email Input
//        TextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // Password Input
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation(),
//            singleLine = true
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Login Button
//        Button(
//            onClick = {
//                isLoggingIn = true
//                auth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener { task ->
//                        isLoggingIn = false
//                        if (task.isSuccessful) {
//                            val userId = auth.currentUser?.uid
//                            if (userId != null) {
//
//                                // Save user ID in SharedPreferences
//                                with(sharedPreferences.edit()) {
//                                    putString("userId", userId)
//                                    apply()
//                                }
//
//                                // Navigate to ActivityPage
//                                navController.navigate("activityPage") {
//                                    popUpTo("LoginScreen") { inclusive = true }
//                                }
//                            } else {
//                                Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
//                            }
//                        } else {
//                            Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoggingIn
//        ) {
//            if (isLoggingIn) {
//                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
//                Spacer(modifier = Modifier.width(10.dp))
//                Text("Logging In...")
//            } else {
//                Text("Login")
//            }
//        }
//    }
//}
