//package com.ejecicio.myapplication.scenes.activities
//
//import android.content.Context
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.google.firebase.firestore.FirebaseFirestore
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.ArrowBack
//
//@Composable
//fun FullActivity(navController: NavHostController) {
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val selectedActivityUid = sharedPreferences.getString("selectedActivityUid", null)
//    val db = FirebaseFirestore.getInstance()
//
//    var activityDetails by remember { mutableStateOf<Activity?>(null) }
//    var creatorDetails by remember { mutableStateOf<Pair<String, String>?>(null) }
//
//    LaunchedEffect(selectedActivityUid) {
//        selectedActivityUid?.let { uid ->
//            db.collection("activities").document(uid)
//                .get()
//                .addOnSuccessListener { document ->
//                    if (document != null && document.exists()) {
//                        val creatorUid = document.getString("creator") ?: ""
//                        activityDetails = Activity(
//                            uid = uid,
//                            title = document.getString("title") ?: "No Title",
//                            category = document.getString("category") ?: "No Category",
//                            description = document.getString("description") ?: "No Description",
//                            price = document.getDouble("price")?.toFloat() ?: 0f,
//                            maxPeople = document.getLong("maxPeople")?.toInt() ?: 0,
//                            date = document.getString("date") ?: "No Date",
//                            time = document.getString("time") ?: "No Time",
//                            duration = document.getString("duration") ?: "No Duration",
//                            locationLink = document.getString("locationLink") ?: "No Link",
//                            otherInfo = document.getString("otherInfo") ?: "No Info",
//                            peopleAdded = document.getLong("peopleAdded")?.toInt() ?: 0,
//                            creator = creatorUid
//                        )
//
//                        // Fetch creator details
//                        db.collection("users").document(creatorUid)
//                            .get()
//                            .addOnSuccessListener { userDocument ->
//                                if (userDocument != null && userDocument.exists()) {
//                                    val name = userDocument.getString("name") ?: "No Name"
//                                    val university = userDocument.getString("university") ?: "No University"
//                                    creatorDetails = name to university
//                                }
//                            }
//                    }
//                }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//        Column {
//            // Back button
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                IconButton(
//                    onClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .size(48.dp)
//                        .background(Color(0xFFE0E0E0), shape = CircleShape)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.Black
//                    )
//                }
//            }
//
//            // Date and time in pink boxes
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//            ) {
//                activityDetails?.let {
//                    PinkBox(label = "Date", value = it.date)
//                    PinkBox(label = "Time", value = it.time)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Creator details box
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .background(Color(0xFFFFC1E3), shape = CircleShape)
//                    .padding(16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        imageVector = Icons.Default.AccountCircle,
//                        contentDescription = "User Icon",
//                        modifier = Modifier
//                            .size(48.dp)
//                            .padding(end = 8.dp),
//                        tint = Color(0xFFE91E63)
//                    )
//                    if (creatorDetails != null) {
//                        Column {
//                            Text(
//                                text = creatorDetails!!.first,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 18.sp,
//                                color = Color.Black
//                            )
//                            Text(
//                                text = creatorDetails!!.second,
//                                fontSize = 14.sp,
//                                color = Color.Gray
//                            )
//                        }
//                    } else {
//                        Text(text = "Loading creator details...", color = Color.Gray)
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Main content
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.TopCenter
//            ) {
//                if (activityDetails != null) {
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = activityDetails!!.title,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center,
//                            color = Color.Black
//                        )
//
//                        Text(
//                            text = activityDetails!!.description,
//                            fontSize = 16.sp,
//                            textAlign = TextAlign.Justify,
//                            color = Color.Gray
//                        )
//
//                        //InfoRow("Category", activityDetails!!.category)
//                        InfoRow("Price", "${activityDetails!!.price}€")
//                        //InfoRow("Max People", activityDetails!!.maxPeople.toString())
//                        InfoRow("Duration", activityDetails!!.duration)
//                        InfoRow("Location Link", activityDetails!!.locationLink)
//                        InfoRow("Other Info", activityDetails!!.otherInfo)
//                        //InfoRow("People Added", activityDetails!!.peopleAdded.toString())
//                    }
//                } else {
//                    Text(text = "Loading activity details...", color = Color.Gray)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PinkBox(label: String, value: String) {
//    Box(
//        modifier = Modifier
//            .background(Color(0xFFFFC1E3), shape = CircleShape)
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "$label: $value",
//            fontWeight = FontWeight.Bold,
//            color = Color.Black,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun InfoRow(label: String, value: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = label,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black,
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            text = value,
////            textAlign = TextAlign.End,
////            color = Color.DarkGray,
////            modifier = Modifier.weight(1f)
////        )
////    }
////}
//
//
//package com.ejecicio.myapplication.scenes.activities
//
//import android.content.Context
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.google.firebase.firestore.FirebaseFirestore
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.ArrowBack

//@Composable
//fun FullActivity(navController: NavHostController) {
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val selectedActivityUid = sharedPreferences.getString("selectedActivityUid", null)
//    val db = FirebaseFirestore.getInstance()
//
//    var activityDetails by remember { mutableStateOf<Activity?>(null) }
//    var creatorDetails by remember { mutableStateOf<Pair<String, String>?>(null) }
//
//    LaunchedEffect(selectedActivityUid) {
//        selectedActivityUid?.let { uid ->
//            db.collection("activities").document(uid)
//                .get()
//                .addOnSuccessListener { document ->
//                    if (document != null && document.exists()) {
//                        val creatorUid = document.getString("creator") ?: ""
//                        activityDetails = Activity(
//                            uid = uid,
//                            title = document.getString("title") ?: "No Title",
//                            category = document.getString("category") ?: "No Category",
//                            description = document.getString("description") ?: "No Description",
//                            price = document.getDouble("price")?.toFloat() ?: 0f,
//                            maxPeople = document.getLong("maxPeople")?.toInt() ?: 0,
//                            date = document.getString("date") ?: "No Date",
//                            time = document.getString("time") ?: "No Time",
//                            duration = document.getString("duration") ?: "No Duration",
//                            locationLink = document.getString("locationLink") ?: "No Link",
//                            otherInfo = document.getString("otherInfo") ?: "No Info",
//                            peopleAdded = document.getLong("peopleAdded")?.toInt() ?: 0,
//                            creator = creatorUid
//                        )
//
//                        // Fetch creator details
//                        db.collection("users").document(creatorUid)
//                            .get()
//                            .addOnSuccessListener { userDocument ->
//                                if (userDocument != null && userDocument.exists()) {
//                                    val name = userDocument.getString("name") ?: "No Name"
//                                    val university = userDocument.getString("university") ?: "No University"
//                                    creatorDetails = name to university
//                                }
//                            }
//                    }
//                }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            //change to colors in pallete
//            .background(Color(0xFFF5F5F5))
//    ) {
//        Column {
//            // Back button
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                IconButton(
//                    onClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .size(48.dp)
//                        //change to colors in pallete
//                        .background(Color(0xFFE0E0E0), shape = CircleShape)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.Black
//                    )
//                }
//            }
//
//            // Main content
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.TopCenter
//            ) {
//                if (activityDetails != null) {
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = activityDetails!!.title,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center,
//                            color = Color.Black
//                        )
//
//                        Text(
//                            text = activityDetails!!.description,
//                            fontSize = 16.sp,
//                            textAlign = TextAlign.Center,
//                            color = Color.Gray
//                        )
//
//                        // Date and Time Boxes
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                        ) {
//                            PinkBox(label = "Date", value = activityDetails!!.date)
//                            PinkBox(label = "Time", value = activityDetails!!.time)
//                        }
//
//                        //InfoRow("Category", activityDetails!!.category)
//                        InfoRow("Price", "${activityDetails!!.price}€")
//                        //InfoRow("Max People", activityDetails!!.maxPeople.toString())
//                        InfoRow("Duration", activityDetails!!.duration)
//                        InfoRow("Location Link", activityDetails!!.locationLink)
//                        InfoRow("Other Info", activityDetails!!.otherInfo)
//                        //InfoRow("People Added", activityDetails!!.peopleAdded.toString())
//
//
//
//                        // Creator details box under People Added
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                                //change to colors in pallete
//                                .background(Color(0xFFFFC1E3), shape = CircleShape)
//                                .padding(16.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(
//                                    imageVector = Icons.Default.AccountCircle,
//                                    contentDescription = "User Icon",
//                                    modifier = Modifier
//                                        .size(48.dp)
//                                        .padding(end = 8.dp),
//                                    //change to colors in pallete
//                                    tint = Color(0xFFE91E63)
//                                )
//                                if (creatorDetails != null) {
//                                    Column {
//                                        Text(
//                                            text = creatorDetails!!.first,
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 18.sp,
//                                            color = Color.Black
//                                        )
//                                        Text(
//                                            text = creatorDetails!!.second,
//                                            fontSize = 14.sp,
//                                            color = Color.Gray
//                                        )
//                                    }
//                                } else {
//                                    Text(text = "Loading creator details...", color = Color.Gray)
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    Text(text = "Loading activity details...", color = Color.Gray)
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center // Esto centra el contenido dentro del Box
//            ) {
//            Button(
//                // Change route when it is created
//                onClick = { navController.navigate("messages???") },
//
//            ) {
//                Text(text = "Join")
//            }}
//
//        }
//    }
////}
//@Composable
//fun FullActivity(navController: NavHostController) {
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val selectedActivityUid = sharedPreferences.getString("selectedActivityUid", null)
//    val userId = sharedPreferences.getString("userId", null) // Get the current user's UID
//    val db = FirebaseFirestore.getInstance()
//
//    var activityDetails by remember { mutableStateOf<Activity?>(null) }
//    var creatorDetails by remember { mutableStateOf<Pair<String, String>?>(null) }
//
//    LaunchedEffect(selectedActivityUid) {
//        selectedActivityUid?.let { uid ->
//            db.collection("activities").document(uid)
//                .get()
//                .addOnSuccessListener { document ->
//                    if (document != null && document.exists()) {
//                        val creatorUid = document.getString("creator") ?: ""
//                        activityDetails = Activity(
//                            uid = uid,
//                            title = document.getString("title") ?: "No Title",
//                            category = document.getString("category") ?: "No Category",
//                            description = document.getString("description") ?: "No Description",
//                            price = document.getDouble("price")?.toFloat() ?: 0f,
//                            maxPeople = document.getLong("maxPeople")?.toInt() ?: 0,
//                            date = document.getString("date") ?: "No Date",
//                            time = document.getString("time") ?: "No Time",
//                            duration = document.getString("duration") ?: "No Duration",
//                            locationLink = document.getString("locationLink") ?: "No Link",
//                            otherInfo = document.getString("otherInfo") ?: "No Info",
//                            peopleAdded = document.getLong("peopleAdded")?.toInt() ?: 0,
//                            creator = creatorUid
//                        )
//
//                        // Fetch creator details
//                        db.collection("users").document(creatorUid)
//                            .get()
//                            .addOnSuccessListener { userDocument ->
//                                if (userDocument != null && userDocument.exists()) {
//                                    val name = userDocument.getString("name") ?: "No Name"
//                                    val university = userDocument.getString("university") ?: "No University"
//                                    creatorDetails = name to university
//                                }
//                            }
//                    }
//                }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//        Column {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                IconButton(
//                    onClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .size(48.dp)
//                        .background(Color(0xFFE0E0E0), shape = CircleShape)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.Black
//                    )
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.TopCenter
//            ) {
//                if (activityDetails != null) {
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = activityDetails!!.title,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center,
//                            color = Color.Black
//                        )
//
//                        Text(
//                            text = activityDetails!!.description,
//                            fontSize = 16.sp,
//                            textAlign = TextAlign.Center,
//                            color = Color.Gray
//                        )
//
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                        ) {
//                            PinkBox(label = "Date", value = activityDetails!!.date)
//                            PinkBox(label = "Time", value = activityDetails!!.time)
//                        }
//
//                        InfoRow("Price", "${activityDetails!!.price}€")
//                        InfoRow("Duration", activityDetails!!.duration)
//                        InfoRow("Location Link", activityDetails!!.locationLink)
//                        InfoRow("Other Info", activityDetails!!.otherInfo)
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                                .background(Color(0xFFFFC1E3), shape = CircleShape)
//                                .padding(16.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(
//                                    imageVector = Icons.Default.AccountCircle,
//                                    contentDescription = "User Icon",
//                                    modifier = Modifier
//                                        .size(48.dp)
//                                        .padding(end = 8.dp),
//                                    tint = Color(0xFFE91E63)
//                                )
//                                if (creatorDetails != null) {
//                                    Column {
//                                        Text(
//                                            text = creatorDetails!!.first,
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 18.sp,
//                                            color = Color.Black
//                                        )
//                                        Text(
//                                            text = creatorDetails!!.second,
//                                            fontSize = 14.sp,
//                                            color = Color.Gray
//                                        )
//                                    }
//                                } else {
//                                    Text(text = "Loading creator details...", color = Color.Gray)
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    Text(text = "Loading activity details...", color = Color.Gray)
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Button(
//                    onClick = {
//                        if (userId != null && selectedActivityUid != null) {
//                            // Find the chat document for the activity
//                            db.collection("chats")
//                                .whereEqualTo("activity", selectedActivityUid)
//                                .get()
//                                .addOnSuccessListener { chatQuerySnapshot ->
//                                    if (!chatQuerySnapshot.isEmpty) {
//                                        val chatDocument = chatQuerySnapshot.documents.first()
//                                        val participants = chatDocument.get("participants") as? List<String> ?: emptyList()
//
//                                        // Add the current user's UID to the participants list
//                                        val updatedParticipants = participants.toMutableList().apply {
//                                            add(userId)
//                                        }
//
//                                        // Update the participants field in the chat document
//                                        db.collection("chats")
//                                            .document(chatDocument.id)
//                                            .update("participants", updatedParticipants)
//                                            .addOnSuccessListener {
//                                                // Optionally, handle success, e.g., show a Toast
//                                            }
//                                            .addOnFailureListener { exception ->
//                                                // Handle failure, e.g., show a Toast
//                                            }
//                                    }
//                                }
//                        }
//                    },
//                ) {
//                    Text(text = "Join")
//                }
//            }
//        }
//    }
//}
package com.ejecicio.myapplication.scenes.activities

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack

//@Composable
//fun FullActivity(navController: NavHostController) {
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val selectedActivityUid = sharedPreferences.getString("selectedActivityUid", null)
//    val userId = sharedPreferences.getString("userId", null)
//    val db = FirebaseFirestore.getInstance()
//
//    var activityDetails by remember { mutableStateOf<Activity?>(null) }
//    var creatorDetails by remember { mutableStateOf<Pair<String, String>?>(null) }
//
//    LaunchedEffect(selectedActivityUid) {
//        selectedActivityUid?.let { uid ->
//            db.collection("activities").document(uid)
//                .get()
//                .addOnSuccessListener { document ->
//                    if (document != null && document.exists()) {
//                        val creatorUid = document.getString("creator") ?: ""
//                        activityDetails = Activity(
//                            uid = uid,
//                            title = document.getString("title") ?: "No Title",
//                            category = document.getString("category") ?: "No Category",
//                            description = document.getString("description") ?: "No Description",
//                            price = document.getDouble("price")?.toFloat() ?: 0f,
//                            maxPeople = document.getLong("maxPeople")?.toInt() ?: 0,
//                            date = document.getString("date") ?: "No Date",
//                            time = document.getString("time") ?: "No Time",
//                            duration = document.getString("duration") ?: "No Duration",
//                            locationLink = document.getString("locationLink") ?: "No Link",
//                            otherInfo = document.getString("otherInfo") ?: "No Info",
//                            peopleAdded = document.getLong("peopleAdded")?.toInt() ?: 0,
//                            creator = creatorUid
//                        )
//
//                        // Fetch creator details
//                        db.collection("users").document(creatorUid)
//                            .get()
//                            .addOnSuccessListener { userDocument ->
//                                if (userDocument != null && userDocument.exists()) {
//                                    val name = userDocument.getString("name") ?: "No Name"
//                                    val university = userDocument.getString("university") ?: "No University"
//                                    creatorDetails = name to university
//                                }
//                            }
//                    }
//                }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//        Column {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                IconButton(
//                    onClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .size(48.dp)
//                        .background(Color(0xFFE0E0E0), shape = CircleShape)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.Black
//                    )
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.TopCenter
//            ) {
//                if (activityDetails != null) {
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = activityDetails!!.title,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center,
//                            color = Color.Black
//                        )
//
//                        Text(
//                            text = activityDetails!!.description,
//                            fontSize = 16.sp,
//                            textAlign = TextAlign.Center,
//                            color = Color.Gray
//                        )
//
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                        ) {
//                            PinkBox(label = "Date", value = activityDetails!!.date)
//                            PinkBox(label = "Time", value = activityDetails!!.time)
//                        }
//
//                        InfoRow("Price", "${activityDetails!!.price}€")
//                        InfoRow("Duration", activityDetails!!.duration)
//                        InfoRow("Location Link", activityDetails!!.locationLink)
//                        InfoRow("Other Info", activityDetails!!.otherInfo)
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                                .background(Color(0xFFFFC1E3), shape = CircleShape)
//                                .padding(16.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(
//                                    imageVector = Icons.Default.AccountCircle,
//                                    contentDescription = "User Icon",
//                                    modifier = Modifier
//                                        .size(48.dp)
//                                        .padding(end = 8.dp),
//                                    tint = Color(0xFFE91E63)
//                                )
//                                if (creatorDetails != null) {
//                                    Column {
//                                        Text(
//                                            text = creatorDetails!!.first,
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 18.sp,
//                                            color = Color.Black
//                                        )
//                                        Text(
//                                            text = creatorDetails!!.second,
//                                            fontSize = 14.sp,
//                                            color = Color.Gray
//                                        )
//                                    }
//                                } else {
//                                    Text(text = "Loading creator details...", color = Color.Gray)
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    Text(text = "Loading activity details...", color = Color.Gray)
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Button(
//                    onClick = {
//                        selectedActivityUid?.let { activityUid ->
//                            userId?.let { uid ->
//                                db.collection("chats")
//                                    .whereEqualTo("activity", activityUid)
//                                    .get()
//                                    .addOnSuccessListener { querySnapshot ->
//                                        if (!querySnapshot.isEmpty) {
//                                            val chatDoc = querySnapshot.documents.first()
//                                            val chatId = chatDoc.id
//                                            val currentParticipants = chatDoc["participants"] as? List<String> ?: emptyList()
//                                            if (!currentParticipants.contains(uid)) {
//                                                val updatedParticipants = currentParticipants + uid
//                                                db.collection("chats").document(chatId)
//                                                    .update("participants", updatedParticipants)
//                                                    .addOnSuccessListener {
//                                                        navController.navigate("messagesPage")
//                                                    }
//                                                    .addOnFailureListener {
//                                                        // Handle update failure
//                                                    }
//                                            } else {
//                                                navController.navigate("messagesPage")
//                                            }
//                                        } else {
//                                            navController.navigate("messagesPage")
//                                        }
//                                    }
//                            }
//                        }
//                    },
//                ) {
//                    Text(text = "Join")
//                }
//            }
//        }
//    }
//}
//
@Composable
fun FullActivity(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val selectedActivityUid = sharedPreferences.getString("selectedActivityUid", null)
    val userId = sharedPreferences.getString("userId", null)
    val db = FirebaseFirestore.getInstance()

    var activityDetails by remember { mutableStateOf<Activity?>(null) }
    var creatorDetails by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(selectedActivityUid) {
        selectedActivityUid?.let { uid ->
            db.collection("activities").document(uid).get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val creatorUid = document.getString("creator") ?: ""
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
                        creator = creatorUid,
                        city = document.getString("city")?: "No City",
                    )

                    db.collection("users").document(creatorUid).get().addOnSuccessListener { userDocument ->
                        if (userDocument != null && userDocument.exists()) {
                            val name = userDocument.getString("name") ?: "No Name"
                            val university = userDocument.getString("university") ?: "No University"
                            creatorDetails = name to university
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = { navController.popBackStack("activityPage", inclusive = false) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Main part of the page
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (activityDetails != null) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = activityDetails!!.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = activityDetails!!.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            PinkBox(label = "Date", value = activityDetails!!.date)
                            PinkBox(label = "Time", value = activityDetails!!.time)
                        }

                        InfoRow("Price", "${activityDetails!!.price}€")
                        InfoRow("Duration", activityDetails!!.duration)
                        InfoRow("Location Link", activityDetails!!.locationLink)
                        InfoRow("Other Info", activityDetails!!.otherInfo)

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(24.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "User Icon",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(end = 8.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                                if (creatorDetails != null) {
                                    Column {
                                        Text(
                                            text = creatorDetails!!.first,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                        Text(
                                            text = creatorDetails!!.second,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Loading creator details...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Loading activity details...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Join butn
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        selectedActivityUid?.let { activityUid ->
                            userId?.let { uid ->
                                db.collection("chats")
                                    .whereEqualTo("activity", activityUid)
                                    .get()
                                    .addOnSuccessListener { querySnapshot ->
                                        if (!querySnapshot.isEmpty) {
                                            val chatDoc = querySnapshot.documents.first()
                                            val chatId = chatDoc.id
                                            val currentParticipants = chatDoc["participants"] as? List<String> ?: emptyList()
                                            if (!currentParticipants.contains(uid)) {
                                                val updatedParticipants = currentParticipants + uid
                                                db.collection("chats").document(chatId)
                                                    .update("participants", updatedParticipants)
                                                    .addOnSuccessListener {
                                                        navController.navigate("messagesPage") {
                                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                            launchSingleTop = true
                                                            restoreState = true
                                                        }
                                                    }
                                            } else {
                                                navController.navigate("messagesPage") {
                                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        } else {
                                            navController.navigate("messagesPage") {
                                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Join",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun PinkBox(label: String, value: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .padding(16.dp)
    ) {
        Text(
            text = "$label: $value",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
    }
}
