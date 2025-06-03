package com.ejecicio.myapplication.scenes.admin

import com.google.firebase.firestore.DocumentReference
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

data class SectionContent(
    val intro: String = "",
    val recommendations: String = "",
    val tip: String = ""
)
@Composable
fun AdminInfoPage(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val sections = listOf(
        "Housing",
        "Transport",
        "Activities and Tourism",
        "Culture and Entertainment",
        "Academic Help and Resources",
        "Food",
        "Emergencies and General Help"
    )

    val sectionsData = remember { mutableStateMapOf<String, SectionContent>() }
    val cityDocRef = remember { mutableStateOf<DocumentReference?>(null) }
    val isAdmin = remember { mutableStateOf(false) }

    // For each field in SectionContent, keep a state per section
    val introState = remember { mutableStateMapOf<String, MutableState<String>>() }
    val recsState = remember { mutableStateMapOf<String, MutableState<String>>() }
    val tipState = remember { mutableStateMapOf<String, MutableState<String>>() }

    LaunchedEffect(currentUser) {
        currentUser?.let {
            val userDocRef = db.collection("users").document(it.uid)
            userDocRef.get().addOnSuccessListener { userSnapshot ->
                val city = userSnapshot.getString("city")
                isAdmin.value = userSnapshot.getBoolean("isAdmin") == true
                if (!city.isNullOrBlank()) {
                    db.collection("info")
                        .whereEqualTo("city", city)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.documents.isNotEmpty()) {
                                val cityRef = snapshot.documents[0].reference
                                cityDocRef.value = cityRef
                                sections.forEach { section ->
                                    cityRef.collection(section)
                                        .get()
                                        .addOnSuccessListener { sectionSnapshot ->
                                            val doc = sectionSnapshot.documents.firstOrNull()
                                            val content = doc?.toObject<SectionContent>()
                                            if (content != null) {
                                                sectionsData[section] = content
                                                introState[section] = mutableStateOf(content.intro)
                                                recsState[section] = mutableStateOf(content.recommendations)
                                                tipState[section] = mutableStateOf(content.tip)
                                            }
                                        }
                                }
                            }
                        }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Admin Panel - Edit Information",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        items(sections) { section ->
            val content = sectionsData[section]
            val intro = introState[section]
            val recs = recsState[section]
            val tip = tipState[section]

            if (content != null && intro != null && recs != null && tip != null) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(section, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isAdmin.value) {
                            OutlinedTextField(
                                value = intro.value,
                                onValueChange = {
                                    intro.value = it
                                    updateSection(section, intro.value, recs.value, tip.value, cityDocRef.value)
                                },
                                label = { Text("Intro") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = recs.value,
                                onValueChange = {
                                    recs.value = it
                                    updateSection(section, intro.value, recs.value, tip.value, cityDocRef.value)
                                },
                                label = { Text("Recommendations") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 4
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = tip.value,
                                onValueChange = {
                                    tip.value = it
                                    updateSection(section, intro.value, recs.value, tip.value, cityDocRef.value)
                                },
                                label = { Text("Pro Tip") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text("Intro: ${content.intro}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Recommendations: ${content.recommendations}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Pro Tip: ${content.tip}")
                        }
                    }
                }
            } else {
                Text("Loading $section...", modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

fun updateSection(
    section: String,
    intro: String,
    recs: String,
    tip: String,
    cityDocRef: DocumentReference?
) {
    if (cityDocRef == null) return
    val sectionRef = cityDocRef.collection(section).document("content")
    val newData = SectionContent(intro, recs, tip)
    sectionRef.set(newData)
}
