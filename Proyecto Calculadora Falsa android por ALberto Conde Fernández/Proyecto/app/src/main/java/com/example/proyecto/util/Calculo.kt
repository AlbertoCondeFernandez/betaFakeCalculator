package com.example.proyecto.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigDecimal
import java.math.RoundingMode

class Calculo {
    companion object { //Usa la función sin crear un objeto
        @RequiresApi(Build.VERSION_CODES.O)
        fun calcular(operacion: String): String {
            val reOperacion =   //Expresión regular para separar operaciones
                Regex("(?<operando1>\\d+(?:\\.\\d+)?)(?<operador>[*%\\-/+])(?<operando2>\\d+(?:\\.\\d+)?)")
            val match = reOperacion.matchEntire(operacion)
            if (match == null)
                throw Exception("La operación no es válida")
            //Toma los valores
            val operando1 = BigDecimal(match.groups.get("operando1")?.value)
            val operador = match.groups.get("operador")?.value
            val operando2 = BigDecimal(match.groups.get("operando2")?.value)

            var resultado: BigDecimal
            try {
                resultado = when (operador) {
                    "*" -> operando1.multiply(operando2)
                    "/" -> operando1.divide(operando2, 10, RoundingMode.HALF_UP)
                    "%" -> operando1.multiply(operando2.divide(BigDecimal(100)))
                    "+" -> operando1.plus(operando2)
                    "-" -> operando1.minus(operando2)
                    else -> BigDecimal.ZERO
                }//Tipo de operaciones
            } catch (e: ArithmeticException) {
                // TODO: Imprimir error completo por log
                throw Exception("Error de cálculo")
            }
            return resultado.toFloat().toString()

            // 4 % 50 -> 2
            // 4 * 1/2 -> 2
            // 20 % 2 -> 0.4
            // 20 * 2 / 100 ->
        }
    }
}

