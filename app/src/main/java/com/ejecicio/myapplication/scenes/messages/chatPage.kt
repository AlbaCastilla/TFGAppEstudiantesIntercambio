import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ejecicio.myapplication.scenes.messages.Message
import com.google.firebase.firestore.Query
import java.util.UUID

//// Componente de burbuja de mensaje
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.userId == currentUserId
    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart

    Log.d("MessageBubble", "Mostrando mensaje de: ${message.userName} - ${message.info}")

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .background(bubbleColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.userName,
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message.info,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(navController: NavHostController, chatId: String) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("userId", null)
    val userName = sharedPreferences.getString("userName", "Unknown")

    var messages by remember { mutableStateOf(listOf<Message>()) }
    var messageText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    Log.d("ChatPage", "Usando chatId: $chatId")

    LaunchedEffect(chatId) {
        db.collection("chats").whereEqualTo("uid", chatId).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val chatDocId = documents.documents[0].id
                    db.collection("chats").document(chatDocId)
                        .collection("messages")
                        .orderBy("timestamp", Query.Direction.ASCENDING) // ordena por fecha ascendente
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.e("ChatPage", "Error al cargar los mensajes", e)
                                return@addSnapshotListener
                            }
                            messages = snapshot?.documents?.mapNotNull { doc ->
                                val timestamp = doc.getString("timestamp") ?: ""
                                doc.toObject(Message::class.java)?.copy(timestamp = timestamp)
                            } ?: emptyList()
                        }
                }
            }
    }

    // desplazamiento hacia el final de la lista cuando los mensajes cambian
    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chat") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("messagesPage") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .imePadding() // Asegura que el contenido se ajusta cuando el teclado aparece
            ) {
                // Lista de mensajes
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { message ->
                        MessageBubble(message = message, currentUserId = userId.orEmpty())
                    }
                }

                // Barra de entrada de texto
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe un mensaje...") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                    )

                    Button(
                        onClick = {
                            if (messageText.isNotBlank() && userId != null) {
                                val newMessage = Message(
                                    uid = UUID.randomUUID().toString(),
                                    timestamp = System.currentTimeMillis().toString(),
                                    info = messageText,
                                    userId = userId,
                                    userName = userName.orEmpty()
                                )

                                db.collection("chats").whereEqualTo("uid", chatId).get()
                                    .addOnSuccessListener { documents ->
                                        if (!documents.isEmpty) {
                                            val chatDocId = documents.documents[0].id
                                            db.collection("chats").document(chatDocId)
                                                .collection("messages")
                                                .document(newMessage.uid)
                                                .set(newMessage)
                                                .addOnSuccessListener { messageText = "" }
                                                .addOnFailureListener { e -> Log.e("ChatPage", "Error al enviar mensaje", e) }
                                        }
                                    }
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Enviar")
                    }
                }
            }
        }
    )
}