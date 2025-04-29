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

//@Composable
//fun ChatPage(navController: NavHostController, chatId: String) {
//    val db = FirebaseFirestore.getInstance()
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val userId = sharedPreferences.getString("userId", null)
//    val userName = sharedPreferences.getString("userName", "Unknown")
//
//    var messages by remember { mutableStateOf(listOf<Message>()) }
//    var messageText by remember { mutableStateOf("") }
//
//    Log.d("ChatPage", "Usando chatId: $chatId")
//
//    LaunchedEffect(chatId) {
//        db.collection("chats").whereEqualTo("uid", chatId).get()
//            .addOnSuccessListener { documents ->
//                if (!documents.isEmpty) {
//                    val chatDocId = documents.documents[0].id
//                    db.collection("chats").document(chatDocId)
//                        .collection("messages")
//                        .orderBy("timestamp")
//                        .addSnapshotListener { snapshot, e ->
//                            if (e != null) {
//                                Log.e("ChatPage", "Error al cargar los mensajes", e)
//                                return@addSnapshotListener
//                            }
//
//                            if (snapshot != null) {
//                                Log.d("ChatPage", "Mensajes cargados: ${snapshot.documents.size}")
//                            }
//                            messages = snapshot?.documents?.mapNotNull { doc ->
//                                val timestamp = doc.getString("timestamp") ?: ""
//                                doc.toObject(Message::class.java)?.copy(timestamp = timestamp)
//                            } ?: emptyList()
//
//                            Log.d("ChatPage", "Mensajes actuales: $messages")
//                        }
//                }
//            }
//    }
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)
//        .imePadding()  // Ajusta el contenido al teclado
//    ) {
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(messages) { message ->
//                MessageBubble(message = message, currentUserId = userId.orEmpty())
//            }
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = messageText,
//                onValueChange = { messageText = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Escribe un mensaje...") },
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        // Hacer algo cuando el usuario presiona "Enter" o "Done"
//                    }
//                ),
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    imeAction = ImeAction.Done
//                )
//            )
//
//            Button(
//                onClick = {
//                    if (messageText.isNotBlank() && userId != null) {
//                        Log.d("ChatPage", "Enviando mensaje: $messageText")
//
//                        val newMessage = Message(
//                            uid = UUID.randomUUID().toString(),
//                            timestamp = System.currentTimeMillis().toString(),
//                            info = messageText,
//                            userId = userId,
//                            userName = userName.orEmpty()
//                        )
//
//                        db.collection("chats").whereEqualTo("uid", chatId).get()
//                            .addOnSuccessListener { documents ->
//                                if (!documents.isEmpty) {
//                                    val chatDocId = documents.documents[0].id
//                                    db.collection("chats").document(chatDocId)
//                                        .collection("messages")
//                                        .document(newMessage.uid)
//                                        .set(newMessage)
//                                        .addOnSuccessListener {
//                                            Log.d("ChatPage", "Mensaje enviado exitosamente: ${newMessage.info}")
//                                            messageText = ""
//                                        }
//                                        .addOnFailureListener { e ->
//                                            Log.e("ChatPage", "Error al enviar el mensaje", e)
//                                        }
//                                }
//                            }
//                    } else {
//                        Log.d("ChatPage", "Mensaje vacío o no hay userId.")
//                    }
//                },
//                modifier = Modifier.padding(start = 8.dp)
//            ) {
//                Text("Enviar")
//            }
//        }
//    }
//}
//
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

//fun ChatPage(navController: NavHostController, chatId: String) {
//    val db = FirebaseFirestore.getInstance()
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val userId = sharedPreferences.getString("userId", null)
//    val userName = sharedPreferences.getString("userName", "Unknown")
//
//    var messages by remember { mutableStateOf(listOf<Message>()) }
//    var messageText by remember { mutableStateOf("") }
//
//    Log.d("ChatPage", "Usando chatId: $chatId")
//
//    LaunchedEffect(chatId) {
//        db.collection("chats").whereEqualTo("uid", chatId).get()
//            .addOnSuccessListener { documents ->
//                if (!documents.isEmpty) {
//                    val chatDocId = documents.documents[0].id
//                    db.collection("chats").document(chatDocId)
//                        .collection("messages")
//                        .orderBy("timestamp")
//                        .addSnapshotListener { snapshot, e ->
//                            if (e != null) {
//                                Log.e("ChatPage", "Error al cargar los mensajes", e)
//                                return@addSnapshotListener
//                            }
//                            messages = snapshot?.documents?.mapNotNull { doc ->
//                                val timestamp = doc.getString("timestamp") ?: ""
//                                doc.toObject(Message::class.java)?.copy(timestamp = timestamp)
//                            } ?: emptyList()
//                        }
//                }
//            }
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Barra superior del chat
//        TopAppBar(
//            title = { Text(text = "Chat") },
//            navigationIcon = {
//                IconButton(onClick = { navController.navigate("messagesPage") }) {
//                    Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
//                }
//            },
//            //backgroundColor = MaterialTheme.colors.primary,
//            //contentColor = Color.White
//        )
//
//        LazyColumn(
//            modifier = Modifier.weight(1f).padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(messages) { message ->
//                MessageBubble(message = message, currentUserId = userId.orEmpty())
//            }
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = messageText,
//                onValueChange = { messageText = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Escribe un mensaje...") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
//            )
//
//            Button(
//                onClick = {
//                    if (messageText.isNotBlank() && userId != null) {
//                        val newMessage = Message(
//                            uid = UUID.randomUUID().toString(),
//                            timestamp = System.currentTimeMillis().toString(),
//                            info = messageText,
//                            userId = userId,
//                            userName = userName.orEmpty()
//                        )
//
//                        db.collection("chats").whereEqualTo("uid", chatId).get()
//                            .addOnSuccessListener { documents ->
//                                if (!documents.isEmpty) {
//                                    val chatDocId = documents.documents[0].id
//                                    db.collection("chats").document(chatDocId)
//                                        .collection("messages")
//                                        .document(newMessage.uid)
//                                        .set(newMessage)
//                                        .addOnSuccessListener { messageText = "" }
//                                        .addOnFailureListener { e -> Log.e("ChatPage", "Error al enviar mensaje", e) }
//                                }
//                            }
//                    }
//                },
//                modifier = Modifier.padding(start = 8.dp)
//            ) {
//                Text("Enviar")
//            }
//        }
//    }
//}

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

    // Estado de la lista
    val listState = rememberLazyListState()

    Log.d("ChatPage", "Usando chatId: $chatId")

    LaunchedEffect(chatId) {
        db.collection("chats").whereEqualTo("uid", chatId).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val chatDocId = documents.documents[0].id
                    db.collection("chats").document(chatDocId)
                        .collection("messages")
                        .orderBy("timestamp", Query.Direction.ASCENDING) // Ordena por fecha ascendente
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

    // Desplazamiento hacia el final de la lista cuando los mensajes cambian
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




//@Composable
//fun ChatPage(navController: NavHostController, chatId: String) {
//    val db = FirebaseFirestore.getInstance()
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val userId = sharedPreferences.getString("userId", null)
//    val userName = sharedPreferences.getString("userName", "Unknown")
//
//    var messages by remember { mutableStateOf(listOf<Message>()) }
//    var messageText by remember { mutableStateOf("") }
//
//    Log.d("ChatPage", "Usando chatId: $chatId")
//
//    LaunchedEffect(chatId) {
//        db.collection("chats").whereEqualTo("uid", chatId).get()
//            .addOnSuccessListener { documents ->
//                if (!documents.isEmpty) {
//                    val chatDocId = documents.documents[0].id
//                    db.collection("chats").document(chatDocId)
//                        .collection("messages")
//                        .orderBy("timestamp")
//                        .addSnapshotListener { snapshot, e ->
//                            if (e != null) {
//                                Log.e("ChatPage", "Error al cargar los mensajes", e)
//                                return@addSnapshotListener
//                            }
//
//                            if (snapshot != null) {
//                                Log.d("ChatPage", "Mensajes cargados: ${snapshot.documents.size}")
//                            }
//                            messages = snapshot?.documents?.mapNotNull { doc ->
//                                val timestamp = doc.getString("timestamp") ?: ""
//                                doc.toObject(Message::class.java)?.copy(timestamp = timestamp)
//                            } ?: emptyList()
//
//                            Log.d("ChatPage", "Mensajes actuales: $messages")
//                        }
//                }
//            }
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(messages) { message ->
//                MessageBubble(message = message, currentUserId = userId.orEmpty())
//            }
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = messageText,
//                onValueChange = { messageText = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Escribe un mensaje...") }
//            )
//
//            Button(
//                onClick = {
//                    if (messageText.isNotBlank() && userId != null) {
//                        Log.d("ChatPage", "Enviando mensaje: $messageText")
//
//                        val newMessage = Message(
//                            uid = UUID.randomUUID().toString(),
//                            timestamp = System.currentTimeMillis().toString(),
//                            info = messageText,
//                            userId = userId,
//                            userName = userName.orEmpty()
//                        )
//
//                        db.collection("chats").whereEqualTo("uid", chatId).get()
//                            .addOnSuccessListener { documents ->
//                                if (!documents.isEmpty) {
//                                    val chatDocId = documents.documents[0].id
//                                    db.collection("chats").document(chatDocId)
//                                        .collection("messages")
//                                        .document(newMessage.uid)
//                                        .set(newMessage)
//                                        .addOnSuccessListener {
//                                            Log.d("ChatPage", "Mensaje enviado exitosamente: ${newMessage.info}")
//                                            messageText = ""
//                                        }
//                                        .addOnFailureListener { e ->
//                                            Log.e("ChatPage", "Error al enviar el mensaje", e)
//                                        }
//                                }
//                            }
//                    } else {
//                        Log.d("ChatPage", "Mensaje vacío o no hay userId.")
//                    }
//                },
//                modifier = Modifier.padding(start = 8.dp)
//            ) {
//                Text("Enviar")
//            }
//        }
//    }
//}
//
//// Componente de burbuja de mensaje
//@Composable
//fun MessageBubble(message: Message, currentUserId: String) {
//    val isCurrentUser = message.userId == currentUserId
//    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray
//    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
//
//    Log.d("MessageBubble", "Mostrando mensaje de: ${message.userName} - ${message.info}")
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
//            Text(
//                text = message.userName,
//                fontSize = 12.sp,
//                color = Color.White,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = message.info,
//                fontSize = 16.sp,
//                color = Color.White
//            )
//        }
//    }
//}


//@Composable
//fun ChatPage(navController: NavHostController, chatId: String) {
//    val db = FirebaseFirestore.getInstance()
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val userId = sharedPreferences.getString("userId", null)
//    val userName = sharedPreferences.getString("userName", "Unknown")
//
//    var messages by remember { mutableStateOf(listOf<Message>()) }
//    var messageText by remember { mutableStateOf("") }
//
//    Log.d("ChatPage", "Usando chatId: $chatId")
//
//    // Cargar mensajes desde Firestore en tiempo real
//    LaunchedEffect(chatId) {
//        db.collection("chats").document(chatId)
//            .collection("messages")
//            .orderBy("timestamp")
//            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
//                if (e != null) {
//                    Log.e("ChatPage", "Error al cargar los mensajes", e)
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null) {
//                    Log.d("ChatPage", "Mensajes cargados: ${snapshot.documents.size}")
//                }
//
////                messages = snapshot?.documents?.mapNotNull { doc ->
////                    val timestamp = doc.getTimestamp("timestamp")?.toDate()?.time?.toString() ?: ""
////                    doc.toObject(Message::class.java)?.copy(timestamp = timestamp)
////                } ?: emptyList()
//                messages = snapshot?.documents?.mapNotNull { doc ->
//                    val timestamp = doc.getString("timestamp") ?: ""  // Extraerlo como String
//                    doc.toObject(Message::class.java)?.copy(timestamp = timestamp)
//                } ?: emptyList()
//
//
//                Log.d("ChatPage", "Mensajes actuales: $messages")
//            }
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(messages) { message ->
//                MessageBubble(message = message, currentUserId = userId.orEmpty())
//            }
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = messageText,
//                onValueChange = { messageText = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Escribe un mensaje...") }
//            )
//
//            Button(
//                onClick = {
//                    if (messageText.isNotBlank() && userId != null) {
//                        Log.d("ChatPage", "Enviando mensaje: $messageText")
//
//                        val newMessage = Message(
//                            uid = UUID.randomUUID().toString(),
//                            timestamp = System.currentTimeMillis().toString(),
//                            info = messageText,
//                            userId = userId,
//                            userName = userName.orEmpty()
//                        )
//
//                        db.collection("chats").document(chatId)
//                            .collection("messages")
//                            .document(newMessage.uid) // Ahora se usa `set` en lugar de `add`
//                            .set(newMessage)
//                            .addOnSuccessListener {
//                                Log.d("ChatPage", "Mensaje enviado exitosamente: ${newMessage.info}")
//                                messageText = ""
//                            }
//                            .addOnFailureListener { e ->
//                                Log.e("ChatPage", "Error al enviar el mensaje", e)
//                            }
//                    } else {
//                        Log.d("ChatPage", "Mensaje vacío o no hay userId.")
//                    }
//                },
//                modifier = Modifier.padding(start = 8.dp)
//            ) {
//                Text("Enviar")
//            }
//        }
//    }
//}


//import android.content.Context
//import android.util.Log
//import androidx.compose.foundation.background
//import com.google.firebase.firestore.FirebaseFirestore
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.ejecicio.myapplication.scenes.messages.Message
//import java.util.UUID
//
//@Composable
//fun ChatPage(navController: NavHostController, chatId: String) {
//    val db = FirebaseFirestore.getInstance()
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val userId = sharedPreferences.getString("userId", null)
//    val userName = sharedPreferences.getString("userName", "Unknown")
//
//    var messages by remember { mutableStateOf(listOf<Message>()) }
//    var messageText by remember { mutableStateOf("") }
//
//    // Log cuando cargamos los mensajes
//    Log.d("ChatPage", "Cargando mensajes de Firestore para el chat: $chatId")
//
//    // Cargar mensajes desde Firestore en tiempo real, ordenados por timestamp
//    LaunchedEffect(chatId) {
//        db.collection("chats").document(chatId)
//            .collection("messages")
//            .orderBy("timestamp") // Ordenar por fecha, más antiguos primero
//            .addSnapshotListener { snapshot, e ->
//                if (e != null) {
//                    // Log si ocurre un error en la carga
//                    Log.e("ChatPage", "Error al cargar los mensajes", e)
//                    return@addSnapshotListener
//                }
//
//                // Log cuando se obtienen mensajes
//                if (snapshot != null) {
//                    Log.d("ChatPage", "Mensajes cargados: ${snapshot.documents.size}")
//                }
//
//                messages = snapshot?.documents?.mapNotNull { doc ->
//                    doc.toObject(Message::class.java)
//                } ?: emptyList()
//
//                // Log para verificar que los mensajes se cargaron
//                Log.d("ChatPage", "Mensajes actuales: $messages")
//            }
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        // Lista de mensajes
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(messages) { message ->
//                MessageBubble(message = message, currentUserId = userId.orEmpty())
//            }
//        }
//
//        // Input para enviar mensajes
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = messageText,
//                onValueChange = { messageText = it },
//                modifier = Modifier.weight(1f),
//                placeholder = { Text("Escribe un mensaje...") }
//            )
//
//            Button(
//                onClick = {
//                    if (messageText.isNotBlank() && userId != null) {
//                        // Log al enviar un mensaje
//                        Log.d("ChatPage", "Enviando mensaje: $messageText")
//
//                        val newMessage = Message(
//                            uid = UUID.randomUUID().toString(),
//                            timestamp = System.currentTimeMillis().toString(),
//                            info = messageText,
//                            userId = userId,
//                            userName = userName.orEmpty()
//                        )
//
//                        db.collection("chats").document(chatId)
//                            .collection("messages")
//                            .add(newMessage)
//                            .addOnSuccessListener {
//                                // Log si el mensaje se envía con éxito
//                                Log.d("ChatPage", "Mensaje enviado exitosamente: ${newMessage.info}")
//                                messageText = "" // Limpiar el campo de texto después de enviar
//                            }
//                            .addOnFailureListener { e ->
//                                // Log si ocurre un error al enviar el mensaje
//                                Log.e("ChatPage", "Error al enviar el mensaje", e)
//                            }
//                    } else {
//                        // Log si el mensaje está vacío o no hay userId
//                        Log.d("ChatPage", "Mensaje vacío o no hay userId.")
//                    }
//                },
//                modifier = Modifier.padding(start = 8.dp)
//            ) {
//                Text("Enviar")
//            }
//        }
//    }
//}
//
//// Componente de burbuja de mensaje
//@Composable
//fun MessageBubble(message: Message, currentUserId: String) {
//    val isCurrentUser = message.userId == currentUserId
//    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray
//    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
//
//    // Log cuando se dibuja una burbuja de mensaje
//    Log.d("MessageBubble", "Mostrando mensaje de: ${message.userName} - ${message.info}")
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
//            Text(
//                text = message.userName,
//                fontSize = 12.sp,
//                color = Color.White,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = message.info,
//                fontSize = 16.sp,
//                color = Color.White
//            )
//        }
//    }
//}
