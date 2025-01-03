package com.ejecicio.myapplication.scenes.activities

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.ejecicio.myapplication.components.FloatingBottomNavBar
//import com.google.firebase.firestore.FirebaseFirestore

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

    // Fetch data from Firebase on first composition
//    LaunchedEffect(Unit) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("activities")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val data = mapOf(
//                        "title" to (document.getString("title") ?: "No Title"),
//                        "date" to (document.getString("date") ?: "No Date"),
//                        "time" to (document.getString("time") ?: "No Time"),
//                        "description" to (document.getString("description") ?: "No Description")
//                    )
//                    activities.add(data)
//                }
//            }
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("activities")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val creatorId = document.getString("creator") ?: ""
                    Log.d("ActivityPage", "Creator ID: $creatorId") // Log the creator ID
                    Log.d("ActivityPage", "Current User ID: $currentUserId") // Log the current user ID
                    if (creatorId != currentUserId) {
                        val data = mapOf(
                            "title" to (document.getString("title") ?: "No Title"),
                            "date" to (document.getString("date") ?: "No Date"),
                            "time" to (document.getString("time") ?: "No Time"),
                            "description" to (document.getString("description") ?: "No Description")
                        )
                        activities.add(data)
                    }
                }
            }
            .addOnFailureListener {
                // Optionally handle errors here
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow)
                .padding(16.dp)
        ) {
            Text(
                text = "Activity Page",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("newActivity") }) {
                Text(text = "Add New Activity")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn for displaying the list of activities
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(activities) { activity ->
                    ActivityCard(
                        title = activity["title"] ?: "",
                        date = activity["date"] ?: "",
                        time = activity["time"] ?: "",
                        description = activity["description"] ?: ""
                    )
                }

                // Add a large spacer after the last item
                item {
                    Spacer(modifier = Modifier.height(116.dp)) // Adjust height as needed
                }
            }
        }

        // Floating Bottom NavBar
        FloatingBottomNavBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ActivityCard(title: String, date: String, time: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: $date",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "Time: $time",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )

        }
    }
}
