package com.ejecicio.myapplication.scenes.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
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
                        .addOnSuccessListener {
                            Toast.makeText(context, "Activity created successfully!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack() // Navigate back
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
    }
}
