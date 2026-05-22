package com.example.proyecto.vistas

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto.composables.OutlinedEmailTextField
import com.example.proyecto.composables.OutlinedPasswordTextField
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth


@Composable
fun AlertDialogExample( //Muestra un cuadro de diálogo para restablecer la contraseña
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Icono")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Sí, enviar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun InicioSesion(navController: NavController, modifier: Modifier = Modifier) {
    val correo = rememberTextFieldState("")        // Guarda lo escribe el usuario
    val contrasenia = rememberTextFieldState("")    // Guarda lo escribe el usuario
    var correoError by remember { mutableStateOf(false) } //reactivo
    var contraseniaError by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") } //mensaje para ver
    val hayError = correoError ||contraseniaError
    var mostraroRecuperarContra by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val manejarContinuar: () -> Unit = {
        showError = true
        val email = correo.text.toString()
        val pass1 = contrasenia.text.toString()
        if (!hayError) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, pass1) //meter .addOnSuccessListener
                .addOnSuccessListener {                     //sin el otro me daba proble
                    // navegar
                    navController.navigate(Vistas.NotaSecreta.ruta)
                }
                .addOnFailureListener { exception ->
                    // Error: correo ya registrado u otro problema
                    //com.google.firebase.auth
                    if (exception is FirebaseAuthInvalidCredentialsException) {
                        mensajeError = "El correo o la contraseña no son correctos. Verificalos o prueba a registrarte"
                    } else {
                        mensajeError = "Error al iniciar sesión"
                    }
                }
        }
    }
    Column (
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Inicio de sesión") //Campo correo reutilizable
        OutlinedEmailTextField(
            state = correo,
            label = { Text("Correo electrónico") },
            showError,
            onValidationChange = { error -> correoError = error },
        )
        OutlinedPasswordTextField( //Campo contraseña reutilizable
            state = contrasenia,
            label = { Text("Contraseña") },
            showError,
            onValidationChange = { error -> contraseniaError = error },
        )
        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = Color.Red)
        }
        Button(onClick = manejarContinuar) {
            Text("Continuar")
        }

        Button(
            onClick = {
                mostraroRecuperarContra = true
            }
        ) {
            Text("Restablecer contraseña")
        }

//mostraroRecuperarContra
        if (mostraroRecuperarContra) {
            AlertDialogExample(
                onDismissRequest = {
                    mostraroRecuperarContra = false
                },
                onConfirmation = {
                    val correoRecuperacion = correo.text.toString().trim()
                    if (correoRecuperacion.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Introduce tu correo para restablecer la contraseña",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@AlertDialogExample
                    }
                    val reCorreo = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
                    if (!correoRecuperacion.matches(reCorreo)) {
                        Toast.makeText(context, "Correo no válido", Toast.LENGTH_SHORT).show()
                        return@AlertDialogExample
                    }
                    Firebase.auth.sendPasswordResetEmail(correoRecuperacion)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Se ha enviado un email para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    mostraroRecuperarContra = false
                },
                dialogTitle = "Recuperar contraseña",
                dialogText = "¿Quieres enviar un correo para recuperar tu contraseña?",
                icon = Icons.Filled.Email
            )


    }

}
}
