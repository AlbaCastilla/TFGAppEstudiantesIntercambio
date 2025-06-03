package com.ejecicio.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun FloatingBottomNavBarAdmin(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val items = listOf("Info", "Activities","Users" )
    val routes = listOf( "adminInfoPage", "adminActivityPage","adminUserPage")

    // Get the current route from the NavController's back stack
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val selectedIndex = routes.indexOf(currentRoute).takeIf { it >= 0 } ?: 0 // Ensure valid index

    // Log the current route for debugging
    LaunchedEffect(currentRoute) {
        println("Current Route: $currentRoute")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = (-50).dp), // Apply offset to push the navbar higher
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(56.dp)
                .shadow(4.dp), // Adjust the height of the navbar
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary // Navbar background color
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, // Ensures items stretch across the row
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                items.forEachIndexed { index, item ->
                    Box( // Use Box to give width flexibility to each item
                        modifier = Modifier
                            .weight(1f) // Distribute each item evenly
                            .fillMaxHeight() // Make the item fill the height of the Row
                            .background(
                                if (index == selectedIndex)
                                    MaterialTheme.colorScheme.secondary // Selected button background
                                else
                                    MaterialTheme.colorScheme.primary // Default button background
                            )
                            .clickable {
                                val targetRoute = routes[index]
                                if (currentRoute != targetRoute) {
                                    println("Navigating from $currentRoute to $targetRoute") // Debug log
                                    navController.navigate(targetRoute) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center // Center the text inside each Box
                    ) {
                        Text(
                            text = item,
                            fontSize = 12.sp, // Scalable pixels
                            color = if (index == selectedIndex)
                                MaterialTheme.colorScheme.onSecondary // Selected text color
                            else
                                MaterialTheme.colorScheme.onPrimary // Unselected text color
                        )
                    }
                }
            }}}}


