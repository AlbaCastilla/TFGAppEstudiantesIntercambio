package com.ejecicio.myapplication.scenes.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.scenes.messages.Chat
import com.ejecicio.myapplication.scenes.messages.Message
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

//@Composable
//fun AddActivityScreen(navController: NavHostController) {
//    val context = LocalContext.current
//    val db = FirebaseFirestore.getInstance()
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val creator = sharedPreferences.getString("userId", null) // Get the user ID of the logged-in user
//
//    var title by remember { mutableStateOf("") }
//    var category by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var price by remember { mutableStateOf("") }
//    var maxPeople by remember { mutableStateOf("") }
//    var duration by remember { mutableStateOf("") }
//    var locationLink by remember { mutableStateOf("") }
//    var otherInfo by remember { mutableStateOf("") }
//    var date by remember { mutableStateOf("") }
//    var time by remember { mutableStateOf("") }
//
//    val isFormValid = title.isNotEmpty() && category.isNotEmpty() && description.isNotEmpty() &&
//            price.isNotEmpty() && maxPeople.isNotEmpty() && duration.isNotEmpty() &&
//            locationLink.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()
//
//    // Date Picker Dialog
//    val calendar = Calendar.getInstance()
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _, year, month, dayOfMonth ->
//            date = "$dayOfMonth/${month + 1}/$year"
//            calendar.set(year, month, dayOfMonth)  // Update the calendar with the selected date
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH)
//    )
//
//    // Time Picker Dialog
//    val timePickerDialog = TimePickerDialog(
//        context,
//        { _, hour, minute ->
//            time = String.format("%02d:%02d", hour, minute)
//            calendar.set(Calendar.HOUR_OF_DAY, hour)
//            calendar.set(Calendar.MINUTE, minute)  // Update the calendar with the selected time
//        },
//        calendar.get(Calendar.HOUR_OF_DAY),
//        calendar.get(Calendar.MINUTE),
//        true
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Create Activity", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Input fields for activity details
//        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = price,
//            onValueChange = { if (it.toFloatOrNull() != null || it.isEmpty()) price = it },
//            label = { Text("Price") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(
//            value = maxPeople,
//            onValueChange = { if (it.toIntOrNull() != null || it.isEmpty()) maxPeople = it },
//            label = { Text("Max People") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(value = locationLink, onValueChange = { locationLink = it }, label = { Text("Location Link") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(10.dp))
//
//        TextField(value = otherInfo, onValueChange = { otherInfo = it }, label = { Text("Other Info (Optional)") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // Date Picker Button
//        Button(onClick = { datePickerDialog.show() }, modifier = Modifier.fillMaxWidth()) {
//            Text(text = if (date.isEmpty()) "Select Date" else date)
//        }
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // Time Picker Button
//        Button(onClick = { timePickerDialog.show() }, modifier = Modifier.fillMaxWidth()) {
//            Text(text = if (time.isEmpty()) "Select Time" else time)
//        }
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Create Activity Button
//        Button(
//            onClick = {
//                if (creator != null) {
//                    // Get the selected date and time from the calendar
//                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
//                    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
//
//                    val newActivity = Activity(
//                        uid = UUID.randomUUID().toString(),
//                        title = title,
//                        category = category,
//                        description = description,
//                        price = price.toFloat(),
//                        maxPeople = maxPeople.toInt(),
//                        date = formattedDate,
//                        time = formattedTime,
//                        duration = duration,
//                        locationLink = locationLink,
//                        otherInfo = otherInfo,
//                        creator = creator // Use the logged-in user's ID
//                    )
//
//                    db.collection("activities")
//                        .add(newActivity)
//                        .addOnSuccessListener {
//                            Toast.makeText(context, "Activity created successfully!", Toast.LENGTH_SHORT).show()
//                            navController.popBackStack() // Navigate back
//                        }
//                        .addOnFailureListener { e ->
//                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                        }
//                } else {
//                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
//                }
//            },
//            enabled = isFormValid,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Create Activity")
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val creator = sharedPreferences.getString("userId", null) // Get the user ID of the logged-in user
    Log.d("See why getting userID workks in this page and not in ActivityPage", "Make sure it works correctly here. Current User ID: $creator") // Log the current user ID

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var maxPeople by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var locationLink by remember { mutableStateOf("") }
    var otherInfo by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    val isFormValid = title.isNotEmpty() && category.isNotEmpty() && description.isNotEmpty() &&
            price.isNotEmpty() && maxPeople.isNotEmpty() && duration.isNotEmpty() &&
            locationLink.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()

    // Dropdown menu options
    val categories = listOf("Sports/Exercise", "Cultural", "Outdoors", "Crafty", "Music", "Volunteer", "Other")
    var expanded by remember { mutableStateOf(false) }

    // Date Picker Dialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = "$dayOfMonth/${month + 1}/$year"
            calendar.set(year, month, dayOfMonth)  // Update the calendar with the selected date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Time Picker Dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            time = String.format("%02d:%02d", hour, minute)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)  // Update the calendar with the selected time
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Activity", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        // Input fields for activity details
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        // Dropdown menu for category
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                },
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            category = option
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = price,
            onValueChange = { if (it.toFloatOrNull() != null || it.isEmpty()) price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = maxPeople,
            onValueChange = { if (it.toIntOrNull() != null || it.isEmpty()) maxPeople = it },
            label = { Text("Max People") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        TextField(value = locationLink, onValueChange = { locationLink = it }, label = { Text("Location Link") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        TextField(value = otherInfo, onValueChange = { otherInfo = it }, label = { Text("Other Info (Optional)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        // Date Picker Button
        Button(onClick = { datePickerDialog.show() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = if (date.isEmpty()) "Select Date" else date)
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Time Picker Button
        Button(onClick = { timePickerDialog.show() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = if (time.isEmpty()) "Select Time" else time)
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Create Activity Button
        Button(
            onClick = {
                if (creator != null) {
                    // Get the selected date and time from the calendar
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

                    val newActivity = Activity(
                        uid = UUID.randomUUID().toString(),
                        title = title,
                        category = category,
                        description = description,
                        price = price.toFloat(),
                        maxPeople = maxPeople.toInt(),
                        date = formattedDate,
                        time = formattedTime,
                        duration = duration,
                        locationLink = locationLink,
                        otherInfo = otherInfo,
                        creator = creator // Use the logged-in user's ID
                    )

                    db.collection("activities")
                        .add(newActivity)
                        .addOnSuccessListener { activityDocument ->
                            // La actividad se ha creado con éxito, ahora creamos el Chat
                            val newChat = Chat(
                                uid = UUID.randomUUID().toString(),
                                activity = activityDocument.id, // ID de la actividad recién creada
                                creator = creator,
                                participants = listOf(creator)
                            )

                            db.collection("chats")
                                .add(newChat)
                                .addOnSuccessListener { chatDocumentRef ->
                                    // Fetch the user's name from the 'users' collection
                                    db.collection("users").document(creator)
                                        .get()
                                        .addOnSuccessListener { userDocument ->
                                            val userName = userDocument.getString("name") ?: "Undefined"

                                            // Crear el primer mensaje de bienvenida en la subcolección "messages" dentro del Chat
                                            val welcomeMessage = Message(
                                                uid = UUID.randomUUID().toString(),
                                                timestamp = System.currentTimeMillis().toString(),
                                                info = "Welcome to the chat!",
                                                userId = creator,
                                                userName = userName // Set the name of the creator as userName
                                            )

                                            chatDocumentRef.collection("messages")
                                                .add(welcomeMessage)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Activity, Chat y Mensaje creados con éxito!", Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack() // Navegar hacia atrás
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "Error creando mensaje: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Error obteniendo el nombre del usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error creando el Chat: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error creando la Actividad: ${e.message}", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Activity")
        }

//        Button(
//            onClick = {
//                if (creator != null) {
//                    // Get the selected date and time from the calendar
//                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
//                    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
//
//                    val newActivity = Activity(
//                        uid = UUID.randomUUID().toString(),
//                        title = title,
//                        category = category,
//                        description = description,
//                        price = price.toFloat(),
//                        maxPeople = maxPeople.toInt(),
//                        date = formattedDate,
//                        time = formattedTime,
//                        duration = duration,
//                        locationLink = locationLink,
//                        otherInfo = otherInfo,
//                        creator = creator // Use the logged-in user's ID
//                    )
//
////                    db.collection("activities")
////                        .add(newActivity)
////                        .addOnSuccessListener {
////                            Toast.makeText(context, "Activity created successfully!", Toast.LENGTH_SHORT).show()
////                            navController.popBackStack() // Navigate back
////                        }
////                        .addOnFailureListener { e ->
////                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
////                        }
//                    // Add the Activity to Firestore
////                    db.collection("activities")
////                        .add(newActivity)
////                        .addOnSuccessListener { activityDocument ->
////                            // Successfully added the activity, now create a Chat object
////                            val newChat = Chat(
////                                uid = UUID.randomUUID().toString(),
////                                activity = activityDocument.id, // Use Firestore's generated activity ID
////                                creator = creator,
////                                participants = listOf(creator)
////                            )
////
////                            // Add the Chat to Firestore
////                            db.collection("chats")
////                                .add(newChat)
////                                .addOnSuccessListener {
////                                    Toast.makeText(context, "Activity and Chat created successfully!", Toast.LENGTH_SHORT).show()
////                                    navController.popBackStack() // Navigate back
////                                }
////                                .addOnFailureListener { e ->
////                                    Toast.makeText(context, "Error creating Chat: ${e.message}", Toast.LENGTH_SHORT).show()
////                                }
////                        }
////                        .addOnFailureListener { e ->
////                            Toast.makeText(context, "Error creating Activity: ${e.message}", Toast.LENGTH_SHORT).show()
////                        }
//                    db.collection("activities")
//                        .add(newActivity)
//                        .addOnSuccessListener { activityDocument ->
//                            // La actividad se ha creado con éxito, ahora creamos el Chat
//                            val newChat = Chat(
//                                uid = UUID.randomUUID().toString(),
//                                activity = activityDocument.id, // ID de la actividad recién creada
//                                creator = creator,
//                                participants = listOf(creator)
//                            )
//
//                            db.collection("chats")
//                                .add(newChat)
//                                .addOnSuccessListener { chatDocumentRef -> // Referencia al nuevo chat
//                                    val chatId = chatDocumentRef.id
//
//
//                                    // Crear el primer mensaje de bienvenida en la subcolección "messages" dentro del Chat
//                                    val welcomeMessage = Message(
//                                        uid = UUID.randomUUID().toString(),
//                                        timestamp = System.currentTimeMillis().toString(), // Timestamp compatible con Firestore
//                                        info = "Welcome to the chat!", // Mensaje de bienvenida
//                                        userId = creator, // El creador envía el mensaje
//                                        userName = "Undefined" // Puedes cambiarlo al nombre del creador si prefieres
//                                    )
//
//                                    chatDocumentRef.collection("messages")
//                                        .add(welcomeMessage)
//                                        .addOnSuccessListener {
//                                            Toast.makeText(context, "Activity, Chat y Mensaje creados con éxito!", Toast.LENGTH_SHORT).show()
//                                            navController.popBackStack() // Navegar hacia atrás
//                                        }
//                                        .addOnFailureListener { e ->
//                                            Toast.makeText(context, "Error creando mensaje: ${e.message}", Toast.LENGTH_SHORT).show()
//                                        }
//                                }
//                                .addOnFailureListener { e ->
//                                    Toast.makeText(context, "Error creando el Chat: ${e.message}", Toast.LENGTH_SHORT).show()
//                                }
//                        }
//                        .addOnFailureListener { e ->
//                            Toast.makeText(context, "Error creando la Actividad: ${e.message}", Toast.LENGTH_SHORT).show()
//                        }
//
//                } else {
//                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
//                }
//            },
//            enabled = isFormValid,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Create Activity")
//        }
//    }
}}
