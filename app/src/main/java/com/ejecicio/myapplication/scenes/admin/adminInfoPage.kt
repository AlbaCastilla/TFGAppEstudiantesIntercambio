package com.ejecicio.myapplication.scenes.admin

import android.util.Log
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
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

data class SectionContent(
    val intro: String = "",
    val reccomendations: String = "",
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


    // For each field in SectionContent, keep a state per section
    val introState = remember { mutableStateMapOf<String, MutableState<String>>() }
    val recsState = remember { mutableStateMapOf<String, MutableState<String>>() }
    val tipState = remember { mutableStateMapOf<String, MutableState<String>>() }

    LaunchedEffect(currentUser) {
        currentUser?.let {
            val userDocRef = db.collection("users").document(it.uid)
            userDocRef.get().addOnSuccessListener { userSnapshot ->
                val city = userSnapshot.getString("city")
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
                                                recsState[section] = mutableStateOf(content.reccomendations)
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
            val reccomendations = recsState[section]
            val tip = tipState[section]
            val context = LocalContext.current


            if (content != null && intro != null && reccomendations != null && tip != null) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(section, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = intro.value,
                                onValueChange = {
                                    intro.value = it
                                    updateSection(context,section, intro.value, reccomendations.value, tip.value, cityDocRef.value)
                                },
                                label = { Text("Intro") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = reccomendations.value,
                                onValueChange = {
                                    reccomendations.value = it
                                    updateSection(context,section, intro.value, reccomendations.value, tip.value, cityDocRef.value)
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
                                    updateSection(context,section, intro.value, reccomendations.value, tip.value, cityDocRef.value)
                                },
                                label = { Text("Pro Tip") },
                                modifier = Modifier.fillMaxWidth()
                            )

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
    context: Context,
    section: String,
    intro: String,
    reccomendations: String,
    tip: String,
    cityDocRef: DocumentReference?
) {
    if (cityDocRef == null) return

    val sectionCollection = cityDocRef.collection(section)
    val newData = SectionContent(intro, reccomendations, tip)

    sectionCollection.get().addOnSuccessListener { querySnapshot ->
        val docRef = querySnapshot.documents.firstOrNull()?.reference
        if (docRef != null) {
            docRef.set(newData).addOnSuccessListener {
                Toast.makeText(context, "Auto-saved $section", Toast.LENGTH_SHORT).show()
            }
        } else {
            sectionCollection.add(newData).addOnSuccessListener {
                Toast.makeText(context, "Auto-saved $section", Toast.LENGTH_SHORT).show()
            }
        }
    }.addOnFailureListener { e ->
        Log.e("FirestoreUpdate", "Error updating section: $section", e)
    }
}
