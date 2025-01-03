package com.ejecicio.myapplication.scenes.activities

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FullActivity(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val selectedActivityUid = sharedPreferences.getString("selectedActivityUid", null)
    val db = FirebaseFirestore.getInstance()

    var activityDetails by remember { mutableStateOf<Activity?>(null) }

    LaunchedEffect(selectedActivityUid) {
        selectedActivityUid?.let { uid ->
            db.collection("activities").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        activityDetails = Activity(
                            uid = uid,
                            title = document.getString("title") ?: "No Title",
                            category = document.getString("category") ?: "No Category",
                            description = document.getString("description") ?: "No Description",
                            price = document.getDouble("price")?.toFloat() ?: 0f,
                            maxPeople = document.getLong("maxPeople")?.toInt() ?: 0,
                            date = document.getString("date") ?: "No Date",
                            time = document.getString("time") ?: "No Time",
                            duration = document.getString("duration") ?: "No Duration",
                            locationLink = document.getString("locationLink") ?: "No Link",
                            otherInfo = document.getString("otherInfo") ?: "No Info",
                            peopleAdded = document.getLong("peopleAdded")?.toInt() ?: 0,
                            creator = document.getString("creator") ?: "No Creator"
                        )
                    } else {
                        Log.d("FullActivity", "No activity found for UID: $uid")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FullActivity", "Error fetching activity: ", exception)
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (activityDetails != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Title: ${activityDetails!!.title}", color = Color.Black)
                Text(text = "Category: ${activityDetails!!.category}", color = Color.Black)
                Text(text = "Description: ${activityDetails!!.description}", color = Color.Black)
                Text(text = "Price: ${activityDetails!!.price}€", color = Color.Black)
                Text(text = "Max People: ${activityDetails!!.maxPeople}", color = Color.Black)
                Text(text = "Date: ${activityDetails!!.date}", color = Color.Black)
                Text(text = "Time: ${activityDetails!!.time}", color = Color.Black)
                Text(text = "Duration: ${activityDetails!!.duration}", color = Color.Black)
                Text(text = "Location Link: ${activityDetails!!.locationLink}", color = Color.Black)
                Text(text = "Other Info: ${activityDetails!!.otherInfo}", color = Color.Black)
                Text(text = "People Added: ${activityDetails!!.peopleAdded}", color = Color.Black)
                Text(text = "Creator: ${activityDetails!!.creator}", color = Color.Black)
            }
        } else {
            Text(text = "Loading activity details...", color = Color.Gray)
        }
    }
}
