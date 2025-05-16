package com.ejecicio.myapplication

import ChatPage
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
import com.ejecicio.myapplication.scenes.superAdmin.AdminCtrl

import com.ejecicio.myapplication.ui.theme.MyApplicationTheme

@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "login") {
        composable("infoPage") {
            MyApplicationTheme {
                InfoPage(navController = navHostController)
        }}

        composable("activityPage") {
            MyApplicationTheme {
                ActivityPage(navController = navHostController)
            }}
        composable("newActivity") {
            MyApplicationTheme {
                AddActivityScreen(navController = navHostController)
            }}

        composable("fullActivity") {
            MyApplicationTheme {
                FullActivity(navController = navHostController)
            }}

        composable("messagesPage") {
            MyApplicationTheme {
                MessagesPage(navController = navHostController)
            }}

//        composable("chatPage") {
//            MyApplicationTheme {
//                ChatPage(navController = navHostController, id:String) // Navigate to the ActivityPage
//            }}
        composable(
            "chatPage/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            MyApplicationTheme {
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatPage(navController = navHostController, chatId = chatId)
            }
        }

        composable("profilePage") {
            MyApplicationTheme {
                ProfilePage(navController = navHostController)
            }}

        composable("forumPage") {
            MyApplicationTheme {
                ForumPage(navController = navHostController)
            }}

        composable("registration") {
            MyApplicationTheme {
                RegisterScreen(navController = navHostController)
            }}

        composable("login") {
            MyApplicationTheme {
                LoginScreen(navController = navHostController)
            }}

          composable("addUniversities") {
            MyApplicationTheme {
                AddUniversities(navController = navHostController)
            }}

        composable("adminCtrl") {
            MyApplicationTheme {
                AdminCtrl(navController = navHostController)
            }}

    }
}
