package com.example.proyecto.vistas

import android.util.Patterns.EMAIL_ADDRESS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto.composables.OutlinedEmailTextField
import com.example.proyecto.composables.OutlinedPasswordTextField
import com.example.proyecto.composables.esPasswordValida
import com.example.proyecto.modelo.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun Registro(navController: NavController, modifier: Modifier = Modifier) {
    val fireDB = FirebaseFirestore.getInstance() //Conecta con la base de datos (Firestore)
    val correo = rememberTextFieldState("")
    val contrasenia = rememberTextFieldState("")
    val contrasenia2 = rememberTextFieldState("")
    var correoError by remember { mutableStateOf(false) } //reactivo
    var contraseniaError by remember { mutableStateOf(false) }
    var contrasenia2Error by remember { mutableStateOf(false) }
    var noCoincidenError by remember { mutableStateOf(false) }
    var personalizadoError by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") } //mensaje para ver
    val hayError = correoError || contraseniaError || contrasenia2Error || noCoincidenError
    val esRojo = hayError || personalizadoError


    LaunchedEffect(Unit) {
        snapshotFlow { contrasenia.text.equals(contrasenia2.text.toString()) }
            .collect { coinciden ->
                noCoincidenError = false
                mensaje = ""
                if (!coinciden) {
                    noCoincidenError = true
                    mensaje = "Las contraseñas no coinciden"
                }
            }
    }


    val manejarContinuar: () -> Unit = {
        showError = true
        val email = correo.text.toString()
        val pass1 = contrasenia.text.toString()

        if (!hayError) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, pass1) //meter .addOnSuccessListener
                .addOnSuccessListener {
                    mensaje = "El usuario se ha registrado correctamente"
                    personalizadoError = false
                    //Crea una nota vacía para este usuario en la base de datos
                    fireDB.collection("notas").document(email).set(Nota()).addOnSuccessListener {
                        navController.navigate(Vistas.NotaSecreta.ruta)
                    }
                }
                .addOnFailureListener { exception ->
                    // Error: correo ya registrado u otro problema
                    //com.google.firebase.auth
                    if (exception is FirebaseAuthUserCollisionException) {
                        mensaje = "El correo ya está registrado"
                    } else {
                        mensaje = "Error al registrar: ${exception.localizedMessage}"
                    }
                    personalizadoError = true
                }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro")
        OutlinedEmailTextField(
            state = correo,
            label = { Text("Correo electrónico") },
            showError,
            onValidationChange = { error -> correoError = error },
        )
        OutlinedPasswordTextField(
            state = contrasenia,
            label = { Text("Contraseña") },
            showError,
            isError = noCoincidenError,
            onValidationChange = { error -> contraseniaError = error },
        )
        OutlinedPasswordTextField(
            state = contrasenia2,
            label = { Text("Repite la contraseña") },
            showError,
            isError = noCoincidenError,
            onValidationChange = { error -> contrasenia2Error = error },
        )
        // Text(if (hayError) "chungo" else "to guay")
        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = if (esRojo) Color.Red else Color.Green)
        }
        Button(onClick = manejarContinuar) {
            Text("Continuar")
        }

    }
}



/*  Ideas en sucio



 IconButton(onClick = {
            // navController.navigate("Calculadora") //vuelvo a calculadora con el botón
        }) {
//            Icon(
//                imageVector = Icons.Filled.Calculate,
//                contentDescription = "Volver a calculadora"
//
//            )
        }








en caso de adonfailurelisener de tener email ya creado en fire con mismo nombre


                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(                sing it
                        correo.text.toString(),
                        contrasenia.text.toString()
                    )

                 */