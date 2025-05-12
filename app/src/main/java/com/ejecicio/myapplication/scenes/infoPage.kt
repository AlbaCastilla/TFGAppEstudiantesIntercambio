package com.ejecicio.myapplication.scenes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.components.FloatingBottomNavBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

data class SectionContent(
    val intro: String = "",
    val reccomendations: String = "",
    val tip: String = ""
)

@Composable
fun InfoPage(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val sectionsData = remember { mutableStateMapOf<String, SectionContent>() }
    val sections = listOf(
        "Housing",
        "Transport",
        "Activities and Tourism",
        "Culture and Entertainment",
        "Academic Help and Resources",
        "Food",
        "Emergencies and General Help"
    )

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val sectionIndexMap = remember { mutableStateMapOf<String, Int>() }

//    LaunchedEffect(Unit) {
//        db.collection("info")
//            .whereEqualTo("city", "Madrid")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (!querySnapshot.isEmpty) {
//                    val madridDoc = querySnapshot.documents[0]
//                    for (section in sections) {
//                        madridDoc.reference.collection(section)
//                            .get()
//                            .addOnSuccessListener { sectionSnapshot ->
//                                val document = sectionSnapshot.documents.firstOrNull()
//                                if (document != null) {
//                                    val content = document.toObject<SectionContent>()
//                                    if (content != null) {
//                                        sectionsData[section] = content
//                                    }
//                                }
//                            }
//                            .addOnFailureListener { exception ->
//                                Log.e("InfoPage", "Error fetching section $section: ${exception.message}")
//                            }
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e("InfoPage", "Error fetching Madrid info: ${exception.message}")
//            }
//    }
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userDocRef = db.collection("users").document(currentUser.uid)
            userDocRef.get()
                .addOnSuccessListener { userSnapshot ->
                    val userCity = userSnapshot.getString("city")
                    if (!userCity.isNullOrBlank()) {
                        db.collection("info")
                            .whereEqualTo("city", userCity)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    val cityDoc = querySnapshot.documents[0]
                                    for (section in sections) {
                                        cityDoc.reference.collection(section)
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
                                Log.e("InfoPage", "Error fetching info for city $userCity: ${exception.message}")
                            }
                    } else {
                        Log.e("InfoPage", "City not found for user ${currentUser.uid}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("InfoPage", "Error fetching user data: ${exception.message}")
                }
        } else {
            Log.e("InfoPage", "No user is logged in.")
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
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
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Discover essential recommendations and tips curated for your experience in Madrid.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    //dropdown menu
                    var expanded by remember { mutableStateOf(false) }
                    var selectedSection by remember { mutableStateOf("Jump to") }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { expanded = true },
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .height(40.dp)
                            ) {
                                Text(selectedSection)
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown Arrow"
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterEnd)
                            ) {
                                sections.forEachIndexed { index, section ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = section,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        },
                                        onClick = {
                                            selectedSection = section
                                            expanded = false
                                            coroutineScope.launch {
                                                val scrollIndex = sectionIndexMap[section]
                                                if (scrollIndex != null) {
                                                    listState.animateScrollToItem(scrollIndex)
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Here is a bit of information to keep in mind and to get you started in your new home!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }

            itemsIndexed(sections) { index, section ->
                // Track index for scroll-to-item mapping
                sectionIndexMap[section] = index + 1 // +1 because header takes position 0

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = section,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val content = sectionsData[section]
                    if (content != null) {
                        Text(
                            text = content.intro,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        content.reccomendations.split("\n").forEach { recommendation ->
                            if (recommendation.isNotEmpty()) {
                                Row(modifier = Modifier.padding(start = 8.dp)) {
                                    Text("• ", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        Text(
                            text = "Pro tip: ${content.tip}",
                            style = MaterialTheme.typography.bodySmall,
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

            item {
                Spacer(modifier = Modifier.height(116.dp))
            }
        }

        FloatingBottomNavBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}
