package com.ejecicio.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ejecicio.myapplication.scenes.partakers.AddUniversities
import com.ejecicio.myapplication.scenes.activities.ActivityPage
import com.ejecicio.myapplication.scenes.ForumPage
import com.ejecicio.myapplication.scenes.InfoPage
import com.ejecicio.myapplication.scenes.messages.MessagesPage
import com.ejecicio.myapplication.scenes.ProfilePage
import com.ejecicio.myapplication.scenes.activities.AddActivityScreen
import com.ejecicio.myapplication.scenes.activities.FullActivity
import com.ejecicio.myapplication.scenes.authentication.LoginScreen
import com.ejecicio.myapplication.scenes.authentication.RegisterScreen
import com.ejecicio.myapplication.scenes.messages.ChatPage
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

//        composable("chatPage") {
//            MyApplicationTheme {
//                ChatPage(navController = navHostController, id:String) // Navigate to the ActivityPage
//            }}
        composable(
            "chatPage/{chatId}", // Define the dynamic route
            arguments = listOf(navArgument("chatId") { type = NavType.StringType }) // Define the argument type
        ) { backStackEntry ->
            MyApplicationTheme {
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatPage(navController = navHostController, chatId = chatId) // Pass chatId to ChatPage
            }
        }

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
