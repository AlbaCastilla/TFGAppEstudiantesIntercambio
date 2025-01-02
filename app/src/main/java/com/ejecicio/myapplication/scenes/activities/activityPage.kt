package com.ejecicio.myapplication.scenes.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar

@Composable
fun ActivityPage(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Yellow box as the main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Activity Page", color = Color.Black)
            Button(onClick = { navController.navigate("newActivity") }) {
                Text(text = "Add New Activity")
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