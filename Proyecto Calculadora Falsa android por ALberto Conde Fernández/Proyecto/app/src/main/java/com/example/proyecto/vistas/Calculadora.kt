package com.example.proyecto.vistas

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyScopeMarker
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyecto.util.Calculo
import com.example.proyecto.util.TemaBotonera
import com.example.proyecto.util.TemaManager
import com.example.proyecto.util.Validador
import com.example.proyecto.util.obtenerTemaBotonera

enum class BotonEspecial { //Decimos cuales son 3 botones especiales
    AC,
    DEL,
    IGUAL,
}

@Composable
fun Pantalla(operacion: String = "0", resultado: String = "0") {
    Column(       //Donde vamos a poner los numeros que vamos escribiendo y el resultado
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Text(operacion)
        Text(resultado, fontSize = 30.sp)
    }
}

@Composable
fun Botonera(
    operacion: String,
    tema: TemaBotonera = TemaBotonera.DEFECTO,
    onKonamiCode: () -> Unit,
    onOperacionChange: (String) -> Unit,
    onEspecialClick: (BotonEspecial) -> Unit
) {
    var konamiCode by remember { mutableStateOf("") }
    val colores = obtenerTemaBotonera(tema)
    val botones = listOf(
        listOf("AC", "DEL", "%", "/"),
        listOf("7", "8", "9", "*"), listOf("4", "5", "6", "-"), listOf("1", "2", "3", "+"), listOf("0", ".", "=")
    )
    val manejarClick = { boton: String ->
        when (boton) {
            "AC" -> {
                konamiCode = ""
                onEspecialClick(BotonEspecial.AC)
            }
            "DEL" -> onEspecialClick(BotonEspecial.DEL)
            "=" -> {
                if (operacion.isEmpty()) {
                    konamiCode += "="
                } else {
                    konamiCode = ""
                    onEspecialClick(BotonEspecial.IGUAL)
                }
            }
            else -> {
                if (konamiCode.startsWith("===")) {
                    konamiCode += boton
                    if (konamiCode.matches(Regex("===1234"))) {
                        onKonamiCode()
                        konamiCode = ""
                    }
                } else {
                    onOperacionChange(boton)
                }
            }
        }
    }

    // leen los colores del tema seleccionado
    fun fondoBoton(boton: String): Color {
        return when (boton) {
            "AC" -> colores.fondoAC
            "DEL" -> colores.fondoDEL
            "=" -> {
                if (konamiCode.startsWith("===")) {
                    colores.fondoIgualKonami
                } else {
                    colores.fondoIgual
                }
            }
            "+", "-", "*", "/", "%" -> colores.fondoOperador
            else -> colores.fondoNormal
        }
    }
    //leer valoraes de tema elegido  sin fire
    fun colorTextoBoton(boton: String): Color {
        return when (boton) {
            "AC" -> colores.textoAC
            "DEL" -> colores.textoDEL
            "=" -> colores.textoIgual
            "+", "-", "*", "/", "%" -> colores.textoOperador
            else -> colores.textoNormal
        }
    }
    LazyColumn(
        Modifier.fillMaxSize(),
    ) {
        items(botones) { fila ->
            Box(
                Modifier.fillParentMaxHeight(1f.div(botones.size))
            ) {
                LazyRow(
                    Modifier.fillMaxSize(),
                ) {
                    items(fila) { boton ->
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(fondoBoton(boton))
                                .clickable(onClick = { manejarClick(boton) })
                                .fillParentMaxWidth(if (boton == "=") 0.5f else 0.25f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                boton,
                                color = colorTextoBoton(boton),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calculadora(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var resultado by remember { mutableStateOf("0") }   //variables reactivas
    var operacion by remember { mutableStateOf("") }
    val intentarCalculo = {
        try {
            resultado = Calculo.calcular(operacion) //Calcula la operación
            operacion = resultado //Reemplaza operación por el resultado
        } catch (e: Exception) {
            resultado = e.message ?: "Error"
            operacion = ""
        }
    }
    val manejarOperacionChange = { valor: String ->
        if (Validador.comprobarOperador(valor) && Validador.comprobarOperacionCompleta(operacion)) {
            intentarCalculo()
        }
        if (Validador.comprobarOperacionParcial(operacion + valor)) {
            operacion += valor // Solo añade  valor si la operación sigue siendo válida
        }
    }
    val manejarEspecial = { especial: BotonEspecial -> //Controla los 3botones especiales
        when (especial) {
            BotonEspecial.AC -> {
                operacion = ""
                resultado = "0"
            }
            BotonEspecial.DEL -> {
                if (!operacion.isEmpty()) {
                    operacion = operacion.take(operacion.length - 1)
                }
            }

            BotonEspecial.IGUAL -> {
                if (Validador.comprobarOperacionCompleta(operacion)) {
                    intentarCalculo()
                }
            }
        }
    }
    val manejarKonami = {//Es la funcón que ejecuta al detectar la secuencia secreta
        Toast.makeText(context, "KONAMI", Toast.LENGTH_LONG).show()
            //Mensaje por pantalla
        navController.navigate(Vistas.MenuSecreto.ruta) //Te lleva a MenuSecreto
    }
    Column(modifier.fillMaxSize()) {
        Pantalla(operacion, resultado)
        Botonera(
            operacion,
            tema = TemaManager.temaActual,
            onOperacionChange = manejarOperacionChange,
            onEspecialClick = manejarEspecial,
            onKonamiCode = manejarKonami
        )
    }
}


/*

Esta función se utilizaba en la primera versión de botonera, que lo que hacía era emitir un evento de onNúmeroClic y
 onOperadorClic y trataba la operación desde la calculadora, pero de una forma demasiado abstracta, tanto que
 complicaba realmente el tratamiento sobre pantalla de la propia operación.

fun calcularResultado(numero1: Int, operador: String, numero2: Int): Int {
    val resultado = when (operador) {
        "+" -> numero1 + numero2
        "-" -> numero1 - numero2
        "*" -> numero1 * numero2
        "/" -> numero1 / numero2
        "%" -> numero1 % numero2
        else -> 0
    }

    return resultado
}

 */





