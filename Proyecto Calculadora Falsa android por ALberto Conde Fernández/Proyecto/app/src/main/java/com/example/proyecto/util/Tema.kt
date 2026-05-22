package com.example.proyecto.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object TemaManager {
    var temaActual by mutableStateOf(TemaBotonera.DEFECTO)
}//Por defecto usará el tema DEFECTO, si cambia la UI se actualiza
enum class TemaBotonera {  //Temas disponibles
    DEFECTO,
    INVIERNO,
    VERANO,
    OTOÑO,
    PRIMAVERA
}
data class ColoresTemaBotonera( //Guarda colores del tema
    val fondoNormal: Color,
    val textoNormal: Color,
    val fondoOperador: Color,
    val textoOperador: Color,
    val fondoAC: Color,
    val textoAC: Color,
    val fondoDEL: Color,
    val textoDEL: Color,
    val fondoIgual: Color,
    val textoIgual: Color,
    val fondoIgualKonami: Color
)

/**
 * Función para cambiar el tema de la botonera
 */
fun obtenerTemaBotonera(tema: TemaBotonera): ColoresTemaBotonera {
    return when (tema) {
        TemaBotonera.DEFECTO -> ColoresTemaBotonera(
            // Respeta tu vista actual
            fondoNormal = Color.hsl(0f, 0f, 0.9f),
            textoNormal = Color.Black,

            // Los operadores antes tenían el mismo fondo que los normales
            fondoOperador = Color.hsl(0f, 0f, 0.9f),
            textoOperador = Color.Red,

            fondoAC = Color.Magenta,
            textoAC = Color.Black,

            fondoDEL = Color.hsl(30f, 1f, 0.6f),
            textoDEL = Color.Black,

            fondoIgual = Color.Cyan,
            textoIgual = Color.Black,

            fondoIgualKonami = Color.Red
        )

        TemaBotonera.INVIERNO -> ColoresTemaBotonera(
            fondoNormal = Color(0xFFE3F2FD),
            textoNormal = Color(0xFF0D47A1),
            fondoOperador = Color(0xFFBBDEFB),
            textoOperador = Color(0xFF1565C0),
            fondoAC = Color(0xFF90CAF9),
            textoAC = Color.White,
            fondoDEL = Color(0xFF81D4FA),
            textoDEL = Color(0xFF003C8F),
            fondoIgual = Color(0xFF4FC3F7),
            textoIgual = Color.White,
            fondoIgualKonami = Color(0xFF01579B)
        )

        TemaBotonera.VERANO -> ColoresTemaBotonera(
            fondoNormal = Color(0xFFFFF9C4),
            textoNormal = Color(0xFF5D4037),
            fondoOperador = Color(0xFFFFE082),
            textoOperador = Color(0xFFE65100),
            fondoAC = Color(0xFFFF8A65),
            textoAC = Color.White,
            fondoDEL = Color(0xFFFFB74D),
            textoDEL = Color.Black,
            fondoIgual = Color(0xFFFFCA28),
            textoIgual = Color.Black,
            fondoIgualKonami = Color(0xFFFF5722)
        )

        TemaBotonera.OTOÑO -> ColoresTemaBotonera(
            fondoNormal = Color(0xFFD7CCC8),
            textoNormal = Color(0xFF4E342E),
            fondoOperador = Color(0xFFFFCC80),
            textoOperador = Color(0xFFBF360C),
            fondoAC = Color(0xFFA1887F),
            textoAC = Color.White,
            fondoDEL = Color(0xFFFFAB91),
            textoDEL = Color(0xFF4E342E),
            fondoIgual = Color(0xFFFF8A65),
            textoIgual = Color.White,
            fondoIgualKonami = Color(0xFFBF360C)
        )

        TemaBotonera.PRIMAVERA -> ColoresTemaBotonera(
            fondoNormal = Color(0xFFE8F5E9),
            textoNormal = Color(0xFF1B5E20),
            fondoOperador = Color(0xFFC8E6C9),
            textoOperador = Color(0xFF2E7D32),
            fondoAC = Color(0xFFF8BBD0),
            textoAC = Color(0xFF880E4F),
            fondoDEL = Color(0xFFFFF59D),
            textoDEL = Color(0xFF827717),
            fondoIgual = Color(0xFFA5D6A7),
            textoIgual = Color(0xFF1B5E20),
            fondoIgualKonami = Color(0xFFEC407A)
        )
    }
}
