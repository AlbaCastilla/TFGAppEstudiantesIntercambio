package com.ejecicio.myapplication.scenes.messages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

import android.util.Log

import java.util.UUID

@Composable
fun ChatPage(navController: NavHostController, chatId: String) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("userId", null)
    val userName = sharedPreferences.getString("userName", "Unknown")

    var messages by remember { mutableStateOf(listOf<Message>()) }
    var messageText by remember { mutableStateOf("") }

    // Log cuando cargamos los mensajes
    Log.d("ChatPage", "Cargando mensajes de Firestore para el chat: $chatId")

    // Cargar mensajes desde Firestore en tiempo real, ordenados por timestamp
    LaunchedEffect(chatId) {
        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp") // Ordenar por fecha, más antiguos primero
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Log si ocurre un error en la carga
                    Log.e("ChatPage", "Error al cargar los mensajes", e)
                    return@addSnapshotListener
                }

                // Log cuando se obtienen mensajes
                if (snapshot != null) {
                    Log.d("ChatPage", "Mensajes cargados: ${snapshot.documents.size}")
                }

                messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Message::class.java)
                } ?: emptyList()

                // Log para verificar que los mensajes se cargaron
                Log.d("ChatPage", "Mensajes actuales: $messages")
            }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Lista de mensajes
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message, currentUserId = userId.orEmpty())
            }
        }

        // Input para enviar mensajes
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
                placeholder = { Text("Escribe un mensaje...") }
            )

            Button(
                onClick = {
                    if (messageText.isNotBlank() && userId != null) {
                        // Log al enviar un mensaje
                        Log.d("ChatPage", "Enviando mensaje: $messageText")

                        val newMessage = Message(
                            uid = UUID.randomUUID().toString(),
                            timestamp = System.currentTimeMillis().toString(),
                            info = messageText,
                            userId = userId,
                            userName = userName.orEmpty()
                        )

                        db.collection("chats").document(chatId)
                            .collection("messages")
                            .add(newMessage)
                            .addOnSuccessListener {
                                // Log si el mensaje se envía con éxito
                                Log.d("ChatPage", "Mensaje enviado exitosamente: ${newMessage.info}")
                                messageText = "" // Limpiar el campo de texto después de enviar
                            }
                            .addOnFailureListener { e ->
                                // Log si ocurre un error al enviar el mensaje
                                Log.e("ChatPage", "Error al enviar el mensaje", e)
                            }
                    } else {
                        // Log si el mensaje está vacío o no hay userId
                        Log.d("ChatPage", "Mensaje vacío o no hay userId.")
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Enviar")
            }
        }
    }
}

// Componente de burbuja de mensaje
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.userId == currentUserId
    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart

    // Log cuando se dibuja una burbuja de mensaje
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

//@Composable
//fun MessageBubble(message: Message, currentUserId: String) {
//    val isCurrentUser = message.userId == currentUserId
//    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray
//    val alignment = if (isCurrentUser) Alignment.End else Alignment.Start
//
//    Box(
//        modifier = Modifier.fillMaxWidth(),
//        contentAlignment = alignment
//    ) {
//        Column(
//            modifier = Modifier
//                .background(bubbleColor, shape = RoundedCornerShape(8.dp))
//                .padding(8.dp)
//                .widthIn(max = 250.dp)
//        ) {
//            Text(text = message.userName, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
//            Text(text = message.info, fontSize = 16.sp, color = Color.White)
//        }
//    }
//}
