package com.ejecicio.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ejecicio.myapplication.scenes.partakers.AddUniversities
import com.ejecicio.myapplication.scenes.ActivityPage
import com.ejecicio.myapplication.scenes.ForumPage
import com.ejecicio.myapplication.scenes.InfoPage
import com.ejecicio.myapplication.scenes.ProfilePage
import com.ejecicio.myapplication.scenes.authentication.RegisterScreen
import com.ejecicio.myapplication.ui.theme.MyApplicationTheme

@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "registration") {
        composable("infoPage") {
            MyApplicationTheme {
                InfoPage(navController = navHostController) // Navigate to the InfoPage
        }}

        composable("activityPage") {
            MyApplicationTheme {
                ActivityPage(navController = navHostController) // Navigate to the ActivityPage
            }}

        composable("profilePage") {
            MyApplicationTheme {
                ProfilePage(navController = navHostController) // Navigate to the ActivityPage
            }}

        composable("forumPage") {
            MyApplicationTheme {
                ForumPage(navController = navHostController) // Navigate to the ActivityPage
            }}

        composable("registration") {
            MyApplicationTheme {
                RegisterScreen(navController = navHostController) // Navigate to the ActivityPage
            }}

          composable("addUniversities") {
            MyApplicationTheme {
                AddUniversities(navController = navHostController) // Navigate to the ActivityPage
            }}

    }
}
