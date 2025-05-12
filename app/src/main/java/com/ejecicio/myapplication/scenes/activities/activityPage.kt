package com.ejecicio.myapplication.scenes.activities

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ActivityPage(navController: NavHostController) {
    val activities = remember { mutableStateListOf<Map<String, String>>() }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val currentUserId = sharedPreferences.getString("userId", null) ?: ""

    val categories = listOf("Sports/Exercise", "Cultural", "Outdoors", "Crafty", "Music", "Volunteer", "Other")
    val expandedCategories = remember { mutableStateMapOf<String, Boolean>() }

//    LaunchedEffect(Unit) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("activities")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val creatorId = document.getString("creator") ?: ""
//                    if (creatorId != currentUserId) {
//                        val data = mapOf(
//                            "uid" to document.id,
//                            "title" to (document.getString("title") ?: "No Title"),
//                            "date" to (document.getString("date") ?: "No Date"),
//                            "time" to (document.getString("time") ?: "No Time"),
//                            "description" to (document.getString("description") ?: "No Description"),
//                            "category" to (document.getString("category") ?: "Other")
//                        )
//                        activities.add(data)
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e("ActivityPage", "Error fetching activities: ${exception.message}")
//            }
//    }
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        // Get the city of the current user
        usersCollection.document(currentUserId)
            .get()
            .addOnSuccessListener { userDoc ->
                val userCity = userDoc.getString("city") ?: ""

                // Now get all activities and filter by city
                db.collection("activities")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val creatorId = document.getString("creator") ?: ""
                            val activityCity = document.getString("city") ?: ""

                            if (creatorId != currentUserId && activityCity == userCity) {
                                val data = mapOf(
                                    "uid" to document.id,
                                    "title" to (document.getString("title") ?: "No Title"),
                                    "date" to (document.getString("date") ?: "No Date"),
                                    "time" to (document.getString("time") ?: "No Time"),
                                    "description" to (document.getString("description") ?: "No Description"),
                                    "category" to (document.getString("category") ?: "Other")
                                )
                                activities.add(data)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ActivityPage", "Error fetching activities: ${exception.message}")
                    }

            }
            .addOnFailureListener { exception ->
                Log.e("ActivityPage", "Error fetching user city: ${exception.message}")
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(46.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Activities",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Discover activities created by students like you, from sightseeing tours to casual meetups, and start building friendships that will make your time in Madrid truly unforgettable.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                categories.forEach { category ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedCategories[category] = !(expandedCategories[category] ?: false)
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (expandedCategories[category] == true)
                                    Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle Category",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                    }

                    if (expandedCategories[category] == true) {
                        val filteredActivities = activities.filter {
                            it["category"]?.equals(category, ignoreCase = true) == true
                        }

                        if (filteredActivities.isEmpty()) {
                            item {
                                Text(
                                    text = "There are currently no activities for this category.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else {
                            items(filteredActivities) { activity ->
                                ActivityCard(
                                    title = activity["title"] ?: "",
                                    date = activity["date"] ?: "",
                                    time = activity["time"] ?: "",
                                    description = activity["description"] ?: "",
                                    uid = activity["uid"] ?: "",
                                    onJoinClick = { selectedUid ->
                                        sharedPreferences.edit()
                                            .putString("selectedActivityUid", selectedUid)
                                            .apply()
                                        navController.navigate("fullActivity")
                                    }
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(116.dp)) }
            }

        }
        Button(
            onClick = { navController.navigate("newActivity") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp, top = 20.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Add New Activity")
        }

        FloatingBottomNavBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ActivityCard(
    title: String,
    date: String,
    time: String,
    description: String,
    uid: String,
    onJoinClick: (String) -> Unit
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
            Button(
                onClick = { onJoinClick(uid) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "View")
            }
        }
    }
}
