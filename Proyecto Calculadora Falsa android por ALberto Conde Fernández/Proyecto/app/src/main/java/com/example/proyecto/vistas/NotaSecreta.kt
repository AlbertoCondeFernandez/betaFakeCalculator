package com.example.proyecto.vistas

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto.composables.OutlinedEmailTextField
import com.example.proyecto.modelo.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun NotaSecreta(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current //para mostar mensajes toast
    val nota = rememberTextFieldState("")//pensar en algo para que mantenga el valor
    val fire = FirebaseFirestore.getInstance() //Conexión con firebase
    val uid = FirebaseAuth.getInstance().currentUser?.email ?: "" //UID del usuario actual
    var correroError by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }
    val destinatario = rememberTextFieldState("")
    // Si la sesión no está iniciada, simplemente redirige a calculadora.
    if (uid.isEmpty()) {
        navController.navigate(Vistas.MenuSecreto.ruta)
    }

    val fireNota = fire.collection("notas").document(uid)

    LaunchedEffect(Unit) {
        // Meter en un efecto, para asociar el listener una sola vez
        fireNota.addSnapshotListener { snapshot, e ->
            val nuevo = (snapshot?.data?.get("nota") ?: "") as String
            nota.edit {
                replace(0, nota.text.length, nuevo)
            }
        }
    }

    //Sincronizado con Firestore
    @OptIn(FlowPreview::class)
    LaunchedEffect(Unit) {
        snapshotFlow { nota.text.toString() }
            .debounce(500L)           // espera 500ms tras el último keystroke
            .distinctUntilChanged()   // evita guardar si el valor no cambió
            .collect sync@{ text ->
                if (text.isEmpty()) return@sync
                fireNota.set(mapOf("nota" to text), SetOptions.merge())
            }
    }

    val enviar: () -> Unit = enviar@{
        val receptor = destinatario.text.toString()
        if (correroError) {
            mostrarError = receptor.isNotEmpty()
            return@enviar
        }

        if (receptor == uid) {
            Toast.makeText(context, "No te lo puedes enviar a ti mismo", Toast.LENGTH_SHORT).show()
            return@enviar
        }

        fireNota.get().addOnSuccessListener { doc ->
            val document = doc.toObject(Nota::class.java)
            if (document === null) return@addOnSuccessListener

            val fireNotaDestinatario = fire.collection("notas").document(receptor)

            fireNotaDestinatario.get().addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    Toast.makeText(
                        context,
                        "Error, el usuario destinatario que has introducido no existe.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }
                val documentReceptor = doc.toObject(Nota::class.java)
                if (documentReceptor === null) return@addOnSuccessListener

                document.compartidoCon.add(receptor)
                documentReceptor.compartidoDe.add(uid)

                fireNota.set(document, SetOptions.merge())
                fireNotaDestinatario.set(documentReceptor, SetOptions.merge())
            }

            destinatario.clearText()
            Toast.makeText(context, "La nota se ha compartido correctamente.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        OutlinedTextField(
            nota,
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = { Text("Nota") }
        )


        OutlinedEmailTextField(
            destinatario,
            showError = mostrarError,
            onValidationChange = { isError -> correroError = isError },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo del destinatario") },
            trailingIcon = {
                IconButton(onClick = enviar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar"
                    )
                }
            }
        )
    }


}
