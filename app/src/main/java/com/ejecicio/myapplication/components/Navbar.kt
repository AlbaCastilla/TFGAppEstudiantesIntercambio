//package com.ejecicio.myapplication.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun FloatingBottomNavBar(
//    modifier: Modifier = Modifier,
//    onItemSelected: (Int) -> Unit // callback to notify parent about the selected index
//) {
//    // State to manage selected index within the bottom navbar
//    var selectedIndex by remember { mutableStateOf(0) }
//    val items = listOf("Home", "Search", "Profile")
//
//    Box(
//        modifier = modifier.fillMaxWidth().height(56.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            shape = MaterialTheme.shapes.medium,
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .height(56.dp)
//                .background(MaterialTheme.colorScheme.primary) // Automatically uses theme primary color
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items.forEachIndexed { index, item ->
//                    IconButton(
//                        onClick = {
//                            selectedIndex = index
//                            onItemSelected(index) // notify parent of selection
//                        }
//                    ) {
//                        Text(
//                            text = item,
//                            color = if (index == selectedIndex) Color.White else Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

//package com.ejecicio.myapplication.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//
//@Composable
//fun FloatingBottomNavBar(
//    navController: NavController,  // Receive NavController here
//    modifier: Modifier = Modifier,
//) {
//    // State to manage selected index within the bottom navbar
//    var selectedIndex by remember { mutableStateOf(0) }
//    val items = listOf("Home", "Search", "Profile")
//
//    Box(
//        modifier = modifier.fillMaxWidth().height(56.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            shape = MaterialTheme.shapes.medium,
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .height(56.dp)
//                .background(MaterialTheme.colorScheme.primary) // Automatically uses theme primary color
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items.forEachIndexed { index, item ->
//                    IconButton(
//                        onClick = {
//                            selectedIndex = index
//                            when (index) {
//                                0 -> navController.navigate("infoPage") // Navigate to InfoPage
//                                1 -> navController.navigate("activityPage") // Navigate to ActivityPage
//                                // You can add more cases if needed for other pages
//                            }
//                        }
//                    ) {
//                        Text(
//                            text = item,
//                            color = if (index == selectedIndex) Color.White else Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

//package com.ejecicio.myapplication.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun FloatingBottomNavBar(
//    modifier: Modifier = Modifier,
//    onItemSelected: (Int) -> Unit  // Callback to notify the parent when an item is selected
//) {
//    var selectedIndex by remember { mutableStateOf(0) }  // Track the selected index
//    val items = listOf("Home", "Search", "Profile")
//
//    Box(
//        modifier = modifier.fillMaxWidth().height(56.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            shape = MaterialTheme.shapes.medium,
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .height(56.dp)
//                .background(MaterialTheme.colorScheme.primary)  // Use theme color
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items.forEachIndexed { index, item ->
//                    IconButton(
//                        onClick = {
//                            selectedIndex = index  // Update internal index
//                            onItemSelected(index)  // Notify the parent with the selected index
//                        }
//                    ) {
//                        Text(
//                            text = item,
//                            color = if (index == selectedIndex) Color.White else Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//    }
//}


//package com.ejecicio.myapplication.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//
//@Composable
//fun FloatingBottomNavBar(
//    navController: NavHostController, // Add NavHostController
//    modifier: Modifier = Modifier
//) {
//    var selectedIndex by remember { mutableStateOf(0) } // Track the selected index
//    val items = listOf("Info", "Activities", "Forum", "Profile")
//    val routes = listOf("infoPage", "activityPage", "forumPage", "profilePage") // Add corresponding routes
//
//    Box(
//        modifier = modifier.fillMaxWidth().height(56.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            shape = MaterialTheme.shapes.medium,
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .height(56.dp)
//                .background(MaterialTheme.colorScheme.primary) // Use theme color
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items.forEachIndexed { index, item ->
//                    IconButton(
//                        onClick = {
//                            selectedIndex = index // Update internal index
//                            navController.navigate(routes[index]) // Navigate to the selected page
//                        }
//                    ) {
//                        Text(
//                            text = item,
//                            color = if (index == selectedIndex) Color.White else Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//    }
//}


package com.ejecicio.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun FloatingBottomNavBar(
    navController: NavHostController, // Add NavHostController
    modifier: Modifier = Modifier
) {
    val items = listOf("Info", "Activities", "Forum", "Profile")
    val routes = listOf("infoPage", "activityPage", "forumPage", "profilePage") // Add corresponding routes

    // Get the current route from the NavController's back stack
    val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route
    val selectedIndex = routes.indexOf(currentRoute).takeIf { it >= 0 } ?: 0 // Ensure valid index

    Box(
        modifier = modifier.fillMaxWidth().height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(56.dp)
                .background(MaterialTheme.colorScheme.primary) // Use theme color
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                items.forEachIndexed { index, item ->
                    IconButton(
                        onClick = {
                            if (currentRoute != routes[index]) { // Prevent duplicate navigation
                                navController.navigate(routes[index]) {
                                    // Avoid creating multiple copies of the same destination in the stack
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) {
                        Text(
                            text = item,
                            color = if (index == selectedIndex) Color.White else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
