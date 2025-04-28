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
        // Query the "info" collection for the document with city "Madrid"
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
                                // Assuming each section has a single document
                                val document = sectionSnapshot.documents.firstOrNull()
                                if (document != null) {
                                    // Extract the section content from the document fields
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
                        text = "Madrid Info",
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = section,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (sectionsData.containsKey(section)) {
                            val content = sectionsData[section]!!
                            Text(
                                text = "Introduction: ${content.intro}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Recommendations: ${content.reccomendations}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tip: ${content.tip}",
                                style = MaterialTheme.typography.bodySmall
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
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Give some extra space at the bottom
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
