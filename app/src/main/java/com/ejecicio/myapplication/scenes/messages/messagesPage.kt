////package com.ejecicio.myapplication.scenes.messages
////
////import androidx.compose.foundation.background
////import androidx.compose.foundation.layout.Box
////import androidx.compose.foundation.layout.fillMaxSize
////import androidx.compose.foundation.layout.padding
////import androidx.compose.material3.Text
////import androidx.compose.runtime.Composable
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.unit.dp
////import androidx.navigation.NavHostController
////import com.ejecicio.myapplication.components.FloatingBottomNavBar
////
////@Composable
////fun MessagesPage(navController: NavHostController) {
////    Box(
////        modifier = Modifier.fillMaxSize()
////    ) {
////        // Red box as the main content
////        Box(
////            modifier = Modifier
////                .fillMaxSize()
////                .background(Color.Magenta)
////                .padding(16.dp),
////            contentAlignment = Alignment.Center
////        ) {
////            Text(text = "Messages Page", color = Color.White)
////        }
////
////        // Floating Bottom NavBar
////        FloatingBottomNavBar(
////            navController = navController, // Pass the navController
////            modifier = Modifier
////                .align(Alignment.BottomCenter) // Place the navbar at the bottom
////                .padding(bottom = 16.dp)
////        )
////    }
////}
////
////
//
//
//package com.ejecicio.myapplication.scenes.messages
//
//import android.content.Context
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.ejecicio.myapplication.components.FloatingBottomNavBar
//import com.google.firebase.firestore.FirebaseFirestore
//
//@Composable
//fun MessagesPage(navController: NavHostController) {
//    // Initialize Firestore and SharedPreferences
//    val context = LocalContext.current
//    val db = FirebaseFirestore.getInstance()
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val creator = sharedPreferences.getString("userId", null)
//
//    // State to store the list of chat UIDs
//    var chatUids by remember { mutableStateOf(listOf<String>()) }
//
//    // Fetch chats from Firestore
//    LaunchedEffect(creator) {
//        if (creator != null) {
//            db.collection("chats")
//                .whereArrayContains("participants", creator)
//                .get()
//                .addOnSuccessListener { documents ->
//                    // Extract the chat UIDs and update the state
//                    val uids = documents.map { it.getString("uid").orEmpty() }
//                    chatUids = uids
//
//                }
//                .addOnFailureListener { e ->
//                    // Handle the error (optional)
//                    e.printStackTrace()
//                }
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // Red box as the main content
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Magenta)
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            if (chatUids.isNotEmpty()) {
//                // Display the list of chat UIDs
//                chatUids.forEach { uid ->
//                    Text(text = "Chat UID: $uid", color = Color.White)
//                }
//            } else {
//                // Display a message if there are no chats
//                Text(text = "No chats found", color = Color.White)
//            }
//        }
//
//        // Floating Bottom NavBar
//        FloatingBottomNavBar(
//            navController = navController, // Pass the navController
//            modifier = Modifier
//                .align(Alignment.BottomCenter) // Place the navbar at the bottom
//                .padding(bottom = 16.dp)
//        )
//    }
//}


package com.ejecicio.myapplication.scenes.messages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    // Initialize Firestore and SharedPreferences
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val creator = sharedPreferences.getString("userId", null)

    // State to store the list of chat UIDs
    var chatUids by remember { mutableStateOf(listOf<String>()) }

    // Fetch chats from Firestore
    LaunchedEffect(creator) {
        if (creator != null) {
            db.collection("chats")
                .whereArrayContains("participants", creator)
                .get()
                .addOnSuccessListener { documents ->
                    // Extract the chat UIDs and update the state
                    val uids = documents.map { it.getString("uid").orEmpty() }
                    chatUids = uids
                }
                .addOnFailureListener { e ->
                    // Handle the error (optional)
                    e.printStackTrace()
                }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Magenta)
                .padding(16.dp)
        ) {
            if (chatUids.isNotEmpty()) {
                // Display the list of chat UIDs in a column
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(chatUids) { uid ->
                        ChatCard(uid = uid)
                    }
                }
            } else {
                // Display a message if there are no chats
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

        // Floating Bottom NavBar
        FloatingBottomNavBar(
            navController = navController, // Pass the navController
            modifier = Modifier
                .align(Alignment.BottomCenter) // Place the navbar at the bottom
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ChatCard(uid: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Chat UID: $uid",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}
