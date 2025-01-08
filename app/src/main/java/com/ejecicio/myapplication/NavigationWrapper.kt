package com.ejecicio.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ejecicio.myapplication.scenes.partakers.AddUniversities
import com.ejecicio.myapplication.scenes.activities.ActivityPage
import com.ejecicio.myapplication.scenes.ForumPage
import com.ejecicio.myapplication.scenes.InfoPage
import com.ejecicio.myapplication.scenes.MessagesPage
import com.ejecicio.myapplication.scenes.ProfilePage
import com.ejecicio.myapplication.scenes.activities.AddActivityScreen
import com.ejecicio.myapplication.scenes.activities.FullActivity
import com.ejecicio.myapplication.scenes.authentication.LoginScreen
import com.ejecicio.myapplication.scenes.authentication.RegisterScreen
import com.ejecicio.myapplication.ui.theme.MyApplicationTheme

@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "login") {
        composable("infoPage") {
            MyApplicationTheme {
                InfoPage(navController = navHostController) // Navigate to the InfoPage
        }}

        composable("activityPage") {
            MyApplicationTheme {
                ActivityPage(navController = navHostController) // Navigate to the ActivityPage
            }}
        composable("newActivity") {
            MyApplicationTheme {
                AddActivityScreen(navController = navHostController) // Navigate to the ActivityPage
            }}

        composable("fullActivity") {
            MyApplicationTheme {
                FullActivity(navController = navHostController) // Navigate to the ActivityPage
            }}

        composable("messagesPage") {
            MyApplicationTheme {
                MessagesPage(navController = navHostController) // Navigate to the ActivityPage
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

        composable("login") {
            MyApplicationTheme {
                LoginScreen(navController = navHostController) // Navigate to the ActivityPage
            }}

          composable("addUniversities") {
            MyApplicationTheme {
                AddUniversities(navController = navHostController) // Navigate to the ActivityPage
            }}

    }
}
