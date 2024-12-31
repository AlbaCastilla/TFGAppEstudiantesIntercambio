package com.ejecicio.myapplication.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar
import com.ejecicio.myapplication.ui.theme.MyApplicationTheme

@Composable
fun ProfilePage(navController: NavHostController) {
// Crear el estado para el modo claro o oscuro
    var isDarkMode by remember { mutableStateOf(false) }

    // Aplicar el tema basado en el estado de modo oscuro
    MyApplicationTheme(isDarkMode = isDarkMode) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            // Contenido principal de la pantalla
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                // Agregar un botón para cambiar entre modo claro y oscuro
                Button(
                    onClick = {
                        isDarkMode = !isDarkMode // Cambiar el estado del modo oscuro al hacer clic
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface, // Fondo del botón
                        contentColor = MaterialTheme.colorScheme.onSurface // Color del texto
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally) // Centra el botón
                        .padding(16.dp)
                ) {
                    Text("Light/Dark mode")
                }
            }
            Text(
                text = "Profile Page",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.Center) // Centra el texto en el Box
                    .padding(top = 50.dp) // Ajuste opcional para mover el texto hacia abajo si es necesario
            )
        // Floating Bottom NavBar
        FloatingBottomNavBar(
            navController = navController, // Pass the navController
            modifier = Modifier
                .align(Alignment.BottomCenter) // Place the navbar at the bottom
                .padding(bottom = 16.dp)
        )
    }
}}
