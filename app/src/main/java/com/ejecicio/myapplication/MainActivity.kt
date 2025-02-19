package com.ejecicio.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ejecicio.myapplication.scenes.InfoPage
import com.ejecicio.myapplication.components.FloatingBottomNavBar

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            navHostController = rememberNavController()
            NavigationWrapper(navHostController)
        }
    }
}
