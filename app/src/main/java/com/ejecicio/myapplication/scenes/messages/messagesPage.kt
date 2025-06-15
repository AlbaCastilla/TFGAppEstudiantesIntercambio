package com.ejecicio.myapplication.scenes.messages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MessagesPage(navController: NavHostController) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val creator = sharedPreferences.getString("userId", null)

    // list to store the list of chat UIDs- starts empty
    var chatUids by remember { mutableStateOf(listOf<String>()) }

    // fetch chats from Firestore
    LaunchedEffect(creator) {
        if (creator != null) {
            db.collection("chats")
                .whereArrayContains("participants", creator)
                .get()
                .addOnSuccessListener { documents ->
                    // Extract the chat UIDs and update the list
                    val uids = documents.map { it.getString("uid").orEmpty() }
                    chatUids = uids
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
        ) {
            if (chatUids.isNotEmpty()) {
                // display the list of chat UIDs in a column
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(chatUids) { uid ->
                        ChatCard(uid = uid, navController = navController)
                    }
                }
            } else {
                // display a message if there are no chats
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No chats found",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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

@Composable
fun ChatCard(uid: String, navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()

    // store the activity title
    var activityTitle by remember { mutableStateOf("Loading...") }

    // Fetch activity title when  uid is passed
    LaunchedEffect(uid) {
        Log.d("ChatCard", "LaunchedEffect triggered with UID: $uid")

        // get  chat document to retrieve the activity UID
        db.collection("chats")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                Log.d("ChatCard", "Document fetched: ${document.id} ") // Log entire document
                val activityUid = document.getString("activity")
                Log.d("Activity id", "Activity id fetched: $activityUid") // Log activity UID

                if (!activityUid.isNullOrEmpty()) {
                    // Log the chat UID and the activity UID
                    Log.d("ChatCard", "Chat UID: $uid, Activity UID: $activityUid")

                    // Retrieve the activity title using the activity UID
                    db.collection("activities")
                        .whereEqualTo("chatId", activityUid) // Use chatId instead of document ID to make sure it is the same
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty) {
                                val title = querySnapshot.documents[0].getString("title")
                                Log.d("ChatCard", "Activity Title fetched: $title")
                                activityTitle = title ?: "Unknown Activity"
                            } else {
                                Log.d("ChatCard", "No matching activity found")
                                activityTitle = "No Activity Found"
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("ChatCard", "Error fetching activity document: ", e)
                            activityTitle = "Error loading activity"
                        }

                } else {
                    Log.d("ChatCard", "No Activity UID found")
                    activityTitle = "No Activity Linked"
                }
            }
            .addOnFailureListener { e ->
                Log.e("ChatCard", "Error fetching chat document: ", e)
                activityTitle = "Error loading chat"
            }
    }


    // card to display the activity title
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { navController.navigate("chatPage/$uid") }, // Navigate to the chat page using chat ID
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Activity: $activityTitle",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}