package com.ejecicio.myapplication.scenes.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val creator = sharedPreferences.getString("userId", null)
    Log.d("See why getting userID works", "Current User ID: $creator")

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
    var formSubmitted by remember { mutableStateOf(false) }

    val isFormValid = title.isNotEmpty() && category.isNotEmpty() && description.isNotEmpty() &&
            price.isNotEmpty() && maxPeople.isNotEmpty() && duration.isNotEmpty() &&
            locationLink.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()

    // Dropdown menu options for types of activities
    val categories = listOf("Sports/Exercise", "Cultural", "Outdoors", "Crafty", "Music", "Volunteer", "Other")
    var expanded by remember { mutableStateOf(false) }

    // Date picker
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = "$dayOfMonth/${month + 1}/$year"
            calendar.set(year, month, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Time oicker
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            time = String.format("%02d:%02d", hour, minute)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
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
        Box(
            modifier = Modifier
                .fillMaxWidth(),
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
            }}
        Text("Create Activity", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            isError = formSubmitted && title.isEmpty()
        )

        // Dropdown menu -category
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                },
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth().padding(vertical = 4.dp),
                shape = MaterialTheme.shapes.medium,
                isError = formSubmitted && category.isEmpty()
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

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            singleLine = false,
            maxLines = 5,
            isError = formSubmitted && description.isEmpty()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { if (it.toFloatOrNull() != null || it.isEmpty()) price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
            isError = formSubmitted && price.isEmpty()
        )

        OutlinedTextField(
            value = maxPeople,
            onValueChange = { if (it.toIntOrNull() != null || it.isEmpty()) maxPeople = it },
            label = { Text("Max People") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
            isError = formSubmitted && maxPeople.isEmpty()
        )

        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            isError = formSubmitted && duration.isEmpty()
        )

        OutlinedTextField(
            value = locationLink,
            onValueChange = { locationLink = it },
            label = { Text("Location Link") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            isError = formSubmitted && locationLink.isEmpty()
        )

        OutlinedTextField(
            value = otherInfo,
            onValueChange = { otherInfo = it },
            label = { Text("Other Info (Optional)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            isError = false,
            singleLine = false,
            maxLines = 3
        )

        // Date pickerbtn
        Button(onClick = { datePickerDialog.show() }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(text = if (date.isEmpty()) "Select Date" else date)
        }

        // Time pickerbtn
        Button(onClick = { timePickerDialog.show() }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(text = if (time.isEmpty()) "Select Time" else time)
        }

        Button(
            onClick = {
                formSubmitted = true
                if (isFormValid && creator != null) {
                    // egt selected date and time from the calendar
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

                    // fetch city of the logged-in user from Firestore
                    db.collection("users").document(creator)
                        .get()
                        .addOnSuccessListener { userDocument ->
                            val userCity = userDocument.getString("city") ?: "Unknown"

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
                                creator = creator,
                                city = userCity
                            )

                            db.collection("activities")
                                .add(newActivity)
                                .addOnSuccessListener { activityDocument ->
                                    // create Chat automatically after activity is created
                                    val newChat = Chat(
                                        uid = UUID.randomUUID().toString(),
                                        activity = activityDocument.id,
                                        creator = creator,
                                        participants = listOf(creator)
                                    )

                                    db.collection("chats")
                                        .add(newChat)
                                        .addOnSuccessListener { chatDocumentRef ->
                                            // add welcome message automatically from the system
                                            val welcomeMessage = Message(
                                                uid = UUID.randomUUID().toString(),
                                                timestamp = System.currentTimeMillis().toString(),
                                                info = "Welcome to the chat!",
                                                userId = "system",
                                                userName = "System"
                                            )

                                            chatDocumentRef.collection("messages")
                                                .add(welcomeMessage)
                                                .addOnSuccessListener {
                                                    Log.d("AddActivity", "Welcome message added to chat")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("AddActivity", "Error adding welcome message: $e")
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("AddActivity", "Error creating chat: $e")
                                        }

                                    // nav to messages page after activity is created and chat is set up
                                    navController.navigate("messagesPage")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("AddActivity", "Error creating activity: $e")
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e("AddActivity", "Error fetching user city: $e")
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            enabled = isFormValid
        ) {
            Text("Create Activity")
        }
    }
}


