package com.example.proyecto.modelo
data class Nota(
    var nota: String = "",
    var compartidoCon: MutableList<String> = mutableListOf(),
    var compartidoDe: MutableList<String> = mutableListOf()
)
/*Esta data class es una clase de datos  usada para guardar información
representando la estructura de las notas*/