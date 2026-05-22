package com.example.proyecto.util

class Validador {
    companion object {
        /**
         * Comprueba si el operando está completo y es válido
         * Ejemplos:
         * - comprobarOperandoCompleto("5") -> true
         * - comprobarOperandoCompleto("5.6") -> true
         * - comprobarOperandoCompleto("5.") -> false
         * - comprobarOperandoCompleto("...5") -> false
         */
        fun comprobarOperandoCompleto(operando: String): Boolean {
            return operando.matches(Regex("\\d+(\\.\\d+)?"))
        }
        /**
         * Comprueba se el operando es válido parcialmente
         * Ejemplos:
         * - comprobarOperandoParcial("5") -> true
         * - comprobarOperandoParcial("5.6") -> true
         * - comprobarOperandoParcial("5.") -> true
         * - comprobarOperandoParcial("5..") -> false
         * - comprobarOperandoParcial("...5") -> false
         */
        fun comprobarOperandoParcial(operando: String): Boolean {
            val esNumParcial = operando.matches(Regex("\\d*")) //ES NUMERO 1º
            val esDecimal = operando.matches(Regex("\\d+\\.?"))
            val esDecimalParcial = operando.matches(Regex("\\d+\\.\\d*"))

            return esNumParcial || esDecimal || esDecimalParcial
        }

        /**
         * Comprueba si la operación está completa y es válida
         * Ejemplos:
         * - comprobarOperacionCompleta("5") -> false
         * - comprobarOperacionCompleta("5.4") -> false
         * - comprobarOperacionCompleta("5*") -> false
         * - comprobarOperacionCompleta("5-2") -> true
         */
        fun comprobarOperacionCompleta(operacion: String): Boolean {
            return operacion.matches(Regex("\\d+(\\.\\d+)?[*%\\-/+]\\d+(\\.\\d+)?"))
        }
        /**
         * Comprueba si la operación es parcial y es válida
         * Ejemplos:
         * - comprobarOperacionParcial("5++") -> false
         * - comprobarOperacionParcial("5.4.") -> false
         * - comprobarOperacionParcial("5*6*") -> false
         * - comprobarOperacionParcial("5-2") -> true
         * - comprobarOperacionParcial("5-") -> true
         */
        fun comprobarOperacionParcial(operacion: String): Boolean {
            if (comprobarOperandoParcial(operacion)) return true
            val esParcial = operacion.matches(Regex("\\d+(\\.\\d+)?[*%\\-/+][^*%\\-/+]*"))
            val segundoOperando = operacion.split(Regex("[*%\\-/+]")).getOrNull(1)
            if (segundoOperando == null) return false
            val esSegundoParcial = esParcial && comprobarOperandoParcial(segundoOperando)
            return esSegundoParcial
        }
        /**
         * Comprueba si un caracter dado es un operador
         * Ejemplos:
         * - comprobarOperador("5") -> false
         * - comprobarOperador("-") -> true
         * - comprobarOperador(".") -> false
         * - comprobarOperador("*") -> true
         */
        fun comprobarOperador(valor: String): Boolean {
            return valor.matches(Regex("[*%\\-/+]"))
        }
    }
}