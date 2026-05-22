package com.example.proyecto.vistas

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto.util.TemaBotonera
import com.example.proyecto.util.TemaManager
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MenuSecreto(navController: NavController, modifier: Modifier = Modifier) {
    val uid = FirebaseAuth.getInstance().currentUser?.email ?: ""
    var toastActual by remember { mutableStateOf<Toast?>(null) } //para ver el toast actual y no crear cola en caso de pulsar varias veces el boton
    val context = LocalContext.current
    val listaTemas = listOf(
        TemaBotonera.DEFECTO,
        TemaBotonera.INVIERNO,
        TemaBotonera.VERANO,
        TemaBotonera.OTOÑO,
        TemaBotonera.PRIMAVERA
    )
    var indiceTema by remember {
        mutableStateOf(listaTemas.indexOf(TemaManager.temaActual).coerceAtLeast(0))
    }
    Column (
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uid.isNotEmpty()) {
            Button(onClick = {
                navController.navigate(Vistas.NotaSecreta.ruta)
            }) {
                Text("Ver mi nota")
            }
        }
        Button(onClick = {
            navController.navigate(Vistas.InicioSesion.ruta)
        }) {
            Text("Iniciar sesión")
        }
        Button(onClick = {
            navController.navigate(Vistas.Registro.ruta)
        }) {
            Text("Registrarse")
        }
        IconButton(onClick = {
            navController.navigate(Vistas.Calculadora.ruta)
        }) {
        }

        Button(onClick = {
            indiceTema = (indiceTema + 1) % listaTemas.size
            TemaManager.temaActual = listaTemas[indiceTema]

            // Cancelar el toast anterior
            toastActual?.cancel()

            // Crear nuevo toast
            toastActual = Toast.makeText(
                context,
                "Tema ${indiceTema + 1}/${listaTemas.size}: ${TemaManager.temaActual.name}",
                Toast.LENGTH_SHORT
            )

            toastActual?.show()

        }) {
            Text("Cambiar tema de calculadora")
        }

    }
}


/*
Atajo crear icono de copia
//            Icon(
//                imageVector = Icons.Filled.Calculate,
//                contentDescription = "Volver a calculadora"
//            )


 */


