package com.example.proyecto.vistas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto.modelo.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.listOf

data class NotaDe(val autor: String, val contenido: String);
@Composable
fun NotaRecibida(nota: NotaDe){
    var contenido = "***";
    var isSelected by remember { mutableStateOf(false) }

    if (isSelected) {
        contenido = nota.contenido
    }
    Row(
        Modifier.padding(8.dp).fillMaxWidth().clickable(onClick = { isSelected = !isSelected }),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
        ) {
        Column (Modifier.weight(11f)) { //Todo en piloto esta row cliclabe  para acortar o agrandar textos
            Text("Autor: ${nota.autor}", color = Color(255, 200, 50))
            Text("Contenido: $contenido", color = Color.Blue)
        }
        Icon(
            imageVector = if (isSelected) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            contentDescription = if (isSelected) "Ocultar contenido" else "Mostrar contenido",
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun NotasCompartidasDe(navController: NavController) {
    val uid = FirebaseAuth.getInstance().currentUser?.email ?: ""
    // Si la sesión no está iniciada, simplemente redirige a calculadora.
    if (uid.isEmpty()) {
        navController.navigate(Vistas.MenuSecreto.ruta)
    }

    val dbFire = FirebaseFirestore.getInstance()
    val notas = remember { mutableStateListOf<NotaDe>() }
    var mostrarCargando by remember { mutableStateOf(true) }
    val fireNota = dbFire.collection("notas").document(uid)

    LaunchedEffect(Unit) {
        fireNota.addSnapshotListener { snapshot, e ->
            notas.clear()
            val mensajeros = (snapshot?.data?.get("compartidoDe") ?: listOf<String>()) as List<*>;
            if (mensajeros.size == 0)
                mostrarCargando = false;
            for (mensajero in mensajeros) {
                //({ mensajero: String ->
                    val fireNotaDestinatario =
                        dbFire.collection("notas").document(mensajero.toString())
                    fireNotaDestinatario.get().addOnSuccessListener { doc ->
                        // closure, por eso aunque eso sea asincrono tiene el contexto del valor de "mensajero"
                        val documentReceptor = doc.toObject(Nota::class.java)
                        if (documentReceptor === null) return@addOnSuccessListener

                        notas += NotaDe(mensajero.toString(), documentReceptor.nota)
                        // Atención, esto es delicado si en algún momento la información de los destinatarios y los usuarios que hay en la nube no están sincronizados.
                        mostrarCargando = notas.size != mensajeros.size
                    }
                //}(mensajero.toString()))
            }
        }
    }
    if (mostrarCargando) {
        Text("Cargando datos...")
        return
    }
    if (notas.size == 0) {
        Text("No te han compartido notas.")
        return
    }
    LazyColumn (
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(notas) { nota ->
            NotaRecibida(nota)
            HorizontalDivider()
        }
    }
}