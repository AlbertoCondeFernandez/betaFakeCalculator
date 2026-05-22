package com.example.proyecto.vistas

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto.modelo.Nota
import com.example.proyecto.util.TemaBotonera
import com.example.proyecto.util.TemaManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun GestionCompartida(navController: NavController) {
    val uid = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val context = LocalContext.current
    var mostrarCargando by remember { mutableStateOf(true) }
    // Si la sesión no está iniciada, simplemente redirige a calculadora.
    if (uid.isEmpty()) {
        navController.navigate(Vistas.MenuSecreto.ruta)
    }
    val dbFire = FirebaseFirestore.getInstance()
    val compartidoCon = remember { mutableStateListOf<String>() }
    val fireNota = dbFire.collection("notas").document(uid)

    LaunchedEffect(Unit) {
        // Meter en un efecto, para asociar el listener una sola vez
        fireNota.addSnapshotListener { snapshot, e ->
            compartidoCon.clear()
            val destinatarios = (snapshot?.data?.get("compartidoCon") ?: listOf<String>()) as List<*>;

            for (destinatario in destinatarios)
                compartidoCon += destinatario.toString()
            mostrarCargando=false
        }
    }
    val eliminar: (destinatatio: String) -> Unit = enviar@{ destinatario ->
        fireNota.get().addOnSuccessListener { doc ->
            val document = doc.toObject(Nota::class.java)
            if (document === null) return@addOnSuccessListener
            val fireNotaDestinatario = dbFire.collection("notas").document(destinatario)
            fireNotaDestinatario.get().addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    Toast.makeText(context, "Error, el usuario destinatario no existe.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                val documentReceptor = doc.toObject(Nota::class.java)
                if (documentReceptor === null) return@addOnSuccessListener
                document.compartidoCon = document.compartidoCon.filter { d -> d != destinatario } as MutableList
                documentReceptor.compartidoDe = documentReceptor.compartidoDe.filter { e -> e != uid } as MutableList
                fireNota.set(document)
                fireNotaDestinatario.set(documentReceptor)
            }

            Toast.makeText(context, "Se ha dejado de copartir tu nota con $destinatario.", Toast.LENGTH_SHORT).show()
        }
    }

    if (mostrarCargando) {
        Text("Cargando destinatarios...")
        return
    }

    if (compartidoCon.size == 0) {
        Text("No has compartido la nota con nadie")
        return
    }

    LazyColumn (
        Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        items(compartidoCon) { destinatario ->
            Row(
                Modifier.padding(8.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(destinatario, modifier = Modifier.weight(11f))
                IconButton({
                    eliminar(destinatario)
                }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Filled.Delete, "Eliminar")
                }
            }
            HorizontalDivider()
        }
    }
}