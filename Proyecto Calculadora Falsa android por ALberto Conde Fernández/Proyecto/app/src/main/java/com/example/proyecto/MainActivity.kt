package com.example.proyecto

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.composables.BarraNav
import com.example.proyecto.ui.theme.ProyectoTheme
import com.example.proyecto.vistas.Calculadora
import com.example.proyecto.vistas.InicioSesion
import com.example.proyecto.vistas.MenuSecreto
import com.example.proyecto.vistas.NotaSecreta
import com.example.proyecto.vistas.Registro
import com.example.proyecto.vistas.Vistas
import com.example.proyecto.composables.BarraAbajo
import com.example.proyecto.vistas.GestionCompartida
import com.example.proyecto.vistas.NotasCompartidasDe

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoTheme {
                AppNav()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNav(modifier: Modifier = Modifier) {      //es el composable que crea el objeto que me pemite navegar
    val navController = rememberNavController()//que arranco al principio
    Scaffold(
        topBar = { BarraNav(navController) },
        bottomBar = { BarraAbajo(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = Vistas.Calculadora.ruta //Empezará por la vista calculadora
        ) {
            composable(route = Vistas.Calculadora.ruta) {
                Calculadora(navController, modifier)
            }
            composable(route = Vistas.MenuSecreto.ruta) {
                MenuSecreto(navController, modifier)
            }
            composable(route = Vistas.InicioSesion.ruta) {
                InicioSesion(navController, modifier)
            }
            composable(route = Vistas.Registro.ruta) {
                Registro(navController, modifier)
            }
            composable(route = Vistas.NotaSecreta.ruta) {
                NotaSecreta(navController, modifier)
            }
            composable(route = Vistas.GestionCompartida.ruta) {
                GestionCompartida(navController)
            }
            composable(route = Vistas.NotasCompartidasDe.ruta) {
                NotasCompartidasDe(navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoTheme {
        AppNav() //el punto de antrada antiguo era calculadora
    }// ahora va a ser el composable que permite navegar que va a mostar calculadora por defecto
}


