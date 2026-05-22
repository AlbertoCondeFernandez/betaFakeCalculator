package com.example.proyecto.vistas

sealed class Vistas(val ruta: String) {
    object Calculadora: Vistas("Calculadora")
    object MenuSecreto: Vistas("Menu Secreto")
    object Registro: Vistas("Registro")
    object InicioSesion: Vistas("Inicio de Sesion")
    object  NotaSecreta: Vistas("Nota Secreta")
    object GestionCompartida: Vistas("Gestionar compartido con")
    object NotasCompartidasDe: Vistas("Lista de notas Compartidas")

}


/*
Este sealed class cada objeto es una vista de la pantalla de la aplicación. Se usa para poder navegar
 */