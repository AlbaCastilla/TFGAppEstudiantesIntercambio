package com.ejecicio.myapplication.scenes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejecicio.myapplication.components.FloatingBottomNavBar

@Composable
fun InfoPage() {
    var selectedIndex by remember { mutableStateOf(0) }
    val items = listOf("Home", "Search", "Profile")

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 96.dp), // Space to ensure content doesn't overlap with navbar
            contentAlignment = Alignment.Center
        ) {
            when (selectedIndex) {
                0 -> Text(text = "This is the Home Page")
                1 -> Text(text = "This is the Search Page")
                2 -> Text(text = "This is the Profile Page")
            }
        }

        // Move the Floating Bottom NavBar to the top of the screen
        FloatingBottomNavBar(
            items = items,
            selectedIndex = selectedIndex,
            onItemSelected = { selectedIndex = it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
        )
    }
}
