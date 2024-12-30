package com.ejecicio.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ejecicio.myapplication.scenes.InfoPage

@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "InfoPage") {
        composable("InfoPage") {
            InfoPage() // Navigate to the InfoPage
        }
    }
}
