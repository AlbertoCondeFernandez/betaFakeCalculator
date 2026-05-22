package com.example.proyecto.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto.vistas.Vistas
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route //Para saber en que pantalla estoy
    val uid = FirebaseAuth.getInstance().currentUser?.uid //Comprobamos si hay un usuario logueado

    if (Vistas.Calculadora.ruta.equals(currentRoute)) {
        return //Si estoy en calculadora no muestra la barra
    }

    val cerrarSesion = {
        FirebaseAuth.getInstance().signOut()
        navController.navigate(Vistas.Calculadora.ruta)
    }

    TopAppBar(
        title = { Text(currentRoute ?: "") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { //Para ir atrás
                Icon(Icons.AutoMirrored.Default.ArrowBack, "Atrás")
            }
        },
        actions = {
            IconButton(onClick = { //Botón de pánico apra volver a la calaculadora
                navController.navigate(Vistas.Calculadora.ruta)
            }) {
                Icon(
                    imageVector = Icons.Filled.Calculate,
                    contentDescription = "Volver a calculadora"
                )
            }
            if (uid != null) { //En caso de que el usuario este loggeado , se verá esta opción
                IconButton(onClick = cerrarSesion) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión"
                    )
                }
            }
        }
    )
}