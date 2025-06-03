package com.ejecicio.myapplication.scenes.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar
import com.ejecicio.myapplication.components.FloatingBottomNavBarAdmin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun AdminUserPage(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    var studentList by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var isLoading by remember { mutableStateOf(true) }
    var studentToDelete by remember { mutableStateOf<Pair<String, String>?>(null) }

    fun loadStudents(adminCity: String) {
        db.collection("users")
            .whereEqualTo("role", "Student")
            .whereEqualTo("city", adminCity)
            .get()
            .addOnSuccessListener { result ->
                val users = result.documents.mapNotNull { doc ->
                    val data = doc.data
                    data?.plus("uid" to doc.id)
                }
                studentList = users
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    val adminCity = document.getString("city")
                    if (!adminCity.isNullOrEmpty()) {
                        loadStudents(adminCity)
                    } else {
                        isLoading = false
                    }
                }
                .addOnFailureListener {
                    isLoading = false
                }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp) // Reserve space for the bottom nav bar
            ) {
                Text(
                    "Students from your city",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )

                if (studentList.isEmpty()) {
                    Text("No students found.", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(studentList) { student ->
                            val name = student["name"] as? String ?: "N/A"
                            val email = student["email"] as? String ?: "N/A"
                            val uid = student["uid"] as? String ?: ""

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Name: $name")
                                        Text("Email: $email")
                                    }
                                    IconButton(onClick = {
                                        studentToDelete = uid to name
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete user"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Bottom NavBar
        FloatingBottomNavBarAdmin(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }



    // Confirmación de eliminación
    studentToDelete?.let { (uid, name) ->
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            title = { Text("Confirm deletion") },
            text = { Text("Are you sure you want to delete $name? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    FirebaseFirestore.getInstance().collection("users").document(uid)
                        .delete()
                        .addOnSuccessListener {
                            studentList = studentList.filterNot { it["uid"] == uid }
                            studentToDelete = null
                        }
                        .addOnFailureListener {
                            studentToDelete = null
                        }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
