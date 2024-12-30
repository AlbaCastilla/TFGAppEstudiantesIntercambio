//package com.ejecicio.myapplication.scenes
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.ejecicio.myapplication.components.FloatingBottomNavBar
//
//@Composable
//fun InfoPage() {
//    var selectedIndex by remember { mutableStateOf(0) }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // Red box as the main content
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Red)
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "Info Page", color = Color.White)
//        }
//
//        // Floating Bottom NavBar (Navbar stays at the top)
//        FloatingBottomNavBar(
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 24.dp), // Adjust position if needed
//            onItemSelected = { selectedIndex = it } // Update selectedIndex on item click
//        )
//    }
//}

//



package com.ejecicio.myapplication.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar

@Composable
fun InfoPage(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Red box as the main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Info Page", color = Color.White)
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


