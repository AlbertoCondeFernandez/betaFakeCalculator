package com.example.proyecto.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto.vistas.Vistas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraAbajo(navController: NavController) {
    val fire = FirebaseFirestore.getInstance()//se importan los 2 conexión a la base de datos
    val uid = FirebaseAuth.getInstance().currentUser?.email ?: "" //usuario actual email su uid
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route //Sirve para saber en que patalla estoy
    // Solo mostrar la barra en NotaSecreta    currentRoute == Vistas.NotaSecreta.ruta
    if (Vistas.NotaSecreta.ruta.equals(currentRoute) && uid.isNotEmpty()) {
        val fireNota = fire.collection("notas").document(uid)
        BottomAppBar(
            actions = {
                //BORRAR
                IconButton({
                    //invoke
                    fireNota.set(mapOf("nota" to ""), SetOptions.merge())
                }) {
                    Icon(Icons.Filled.Delete, "Borrar")
                }
                //MAYUS
                IconButton({
                    fireNota.get()
                        .addOnSuccessListener { doc ->
                            val texto = doc.getString("nota") ?: ""
                            val nuevo = texto.uppercase()

                            fireNota.set(mapOf("nota" to nuevo), SetOptions.merge())
                        }
                }) {
                    Icon(Icons.Filled.TextIncrease, "Mayús")
                }
                //MINUS
                IconButton({
                    fireNota.get()
                        .addOnSuccessListener { doc ->
                            val texto = doc.getString("nota") ?: ""
                            val nuevo = texto.lowercase()

                            fireNota.set(mapOf("nota" to nuevo), SetOptions.merge())
                        }
                }) {
                    Icon(Icons.Filled.TextDecrease, "Minús")
                }//Ver destinatarios
                IconButton({
                    navController.navigate(Vistas.GestionCompartida.ruta)
                }) {
                    Icon(Icons.Filled.Face, "Ver destinatarios")
                }//er notas compartidas
                IconButton({
                    navController.navigate(Vistas.NotasCompartidasDe.ruta)
                }) {
                    Icon(Icons.AutoMirrored.Filled.LibraryBooks, "Ver notas compartidas")
                }
            }
        )
    }
}

//no borro el texto , borro el valor en la base de datow