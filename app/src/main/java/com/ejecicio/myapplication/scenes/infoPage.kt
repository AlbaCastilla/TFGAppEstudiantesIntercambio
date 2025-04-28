package com.ejecicio.myapplication.scenes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// Data class to hold our section data from the Firestore documents
data class SectionContent(
    val intro: String = "",
    val reccomendations: String = "",
    val tip: String = ""
)

@Composable
fun InfoPage(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()

    // Map of section title to its SectionContent data
    val sectionsData = remember { mutableStateMapOf<String, SectionContent>() }

    // List of subcollections you want to fetch
    val sections = listOf(
        "Housing",
        "Transport",
        "Activities and Tourism",
        "Culture and Entertainment",
        "Academic Help and Resources",
        "Food",
        "Emergencies and General Help"
    )

    LaunchedEffect(Unit) {

        db.collection("info")
            .whereEqualTo("city", "Madrid")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Use the first matching document
                    val madridDoc = querySnapshot.documents[0]
                    // For each section, fetch its data from the respective subcollection
                    for (section in sections) {
                        madridDoc.reference.collection(section)
                            .get()
                            .addOnSuccessListener { sectionSnapshot ->
                                val document = sectionSnapshot.documents.firstOrNull()
                                if (document != null) {
                                    val content = document.toObject<SectionContent>()
                                    if (content != null) {
                                        sectionsData[section] = content
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("InfoPage", "Error fetching section $section: ${exception.message}")
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("InfoPage", "Error fetching Madrid info: ${exception.message}")
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(46.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Madrid",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Discover essential recommendations and tips curated for your experience in Madrid.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            // Iterate over each section to display its info (or a Loading message if not ready)
            items(sections) { section ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = section,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (sectionsData.containsKey(section)) {
                        val content = sectionsData[section]!!

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = content.intro,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        content.reccomendations.split("\n").forEach { recommendation ->
                            if (recommendation.isNotEmpty()) {
                                Row(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "• ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = recommendation.trim(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = content.tip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Loading $section information...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(116.dp)) }
        }

        FloatingBottomNavBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}
