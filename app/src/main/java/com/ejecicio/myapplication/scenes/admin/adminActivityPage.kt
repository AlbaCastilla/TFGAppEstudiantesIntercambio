package com.ejecicio.myapplication.scenes.admin

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBarAdmin
import com.google.firebase.firestore.FirebaseFirestore

val activities = mutableListOf<Map<String, String>>()

@Composable
fun ActivityCardAdmin(
    title: String,
    date: String,
    time: String,
    description: String,
    uid: String,
    onDeleteClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Date: $date", style = MaterialTheme.typography.bodyMedium)
            Text("Time: $time", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onDeleteClick(uid) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Activity",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

fun deleteActivityById(activityId: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("activities").document(activityId)
        .delete()
        .addOnSuccessListener {
            Log.d("ActivityPage", "Successfully deleted activity with ID: $activityId")
        }
        .addOnFailureListener { e ->
            Log.e("ActivityPage", "Error deleting activity: ${e.message}")
        }
}


@Composable
fun ActivityListAdmin(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val currentUserId = sharedPreferences.getString("userId", null) ?: ""

    val db = FirebaseFirestore.getInstance()

    var activityList by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var isLoading by remember { mutableStateOf(true) }
    var activityToDelete by remember { mutableStateOf<Pair<String, String>?>(null) }

    fun loadActivities(userCity: String) {
        db.collection("activities")
            .whereEqualTo("city", userCity)
            .get()
            .addOnSuccessListener { result ->
                val activities = result.documents.map { document ->
                    mapOf(
                        "uid" to document.id,
                        "title" to (document.getString("title") ?: "No Title"),
                        "date" to (document.getString("date") ?: "No Date"),
                        "time" to (document.getString("time") ?: "No Time"),
                        "description" to (document.getString("description") ?: "No Description")
                    )
                }
                activityList = activities
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    LaunchedEffect(currentUserId) {
        db.collection("users").document(currentUserId).get()
            .addOnSuccessListener { document ->
                val userCity = document.getString("city")
                if (!userCity.isNullOrEmpty()) {
                    loadActivities(userCity)
                } else {
                    isLoading = false
                }
            }
            .addOnFailureListener {
                isLoading = false
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
                    .padding(bottom = 72.dp)
            ) {
                Text(
                    "Activities from your city",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )

                if (activityList.isEmpty()) {
                    Text("No activities found.", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(activityList) { activity ->
                            val uid = activity["uid"] ?: ""
                            val title = activity["title"] ?: ""
                            val date = activity["date"] ?: ""
                            val time = activity["time"] ?: ""
                            val description = activity["description"] ?: ""

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(title, style = MaterialTheme.typography.titleLarge)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Date: $date", style = MaterialTheme.typography.bodyMedium)
                                    Text("Time: $time", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(description, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(onClick = {
                                            activityToDelete = uid to title
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete activity",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingBottomNavBarAdmin(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    // Confirm deletion popuo
    activityToDelete?.let { (uid, title) ->
        AlertDialog(
            onDismissRequest = { activityToDelete = null },
            title = { Text("Confirm deletion") },
            text = { Text("Are you sure you want to delete \"$title\"? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    db.collection("activities").document(uid)
                        .delete()
                        .addOnSuccessListener {
                            activityList = activityList.filterNot { it["uid"] == uid }
                            activityToDelete = null
                        }
                        .addOnFailureListener {
                            activityToDelete = null
                        }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { activityToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
