package com.example.proyecto.composables

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

fun esPasswordValida(password: String): Boolean {
    val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$") //1mayuscula 1minuscula 1numero y 8 caracteres
    return regex.matches(password)
}
@Composable
fun OutlinedPasswordTextField(
    state: TextFieldState, //guarda lo que escribe el usuario
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    showError: Boolean = false,
    isError: Boolean = false,
    onValidationChange: (isError: Boolean) -> Unit = {}
) {
    var showPass by remember { mutableStateOf(false) } //controlar si la contraseña es visible o no
    var passValida by remember { mutableStateOf(false) } //si la contra es valida , se guarda
    // Observa cambios en el texto y notifica al padre
    LaunchedEffect(Unit) {
        snapshotFlow { state.text.toString() }
            .collect { text ->
                passValida = esPasswordValida(text) /*passValida = text.length >= 6*/
                onValidationChange(!passValida)
            }
    }
    OutlinedSecureTextField(
        state = state,
        label = label,
        isError = showError && (!passValida || isError), // ponerse en rojo cuando iserror sea true contraseña2
        supportingText = {
            if (showError && !passValida) {
                Text("La contraseña debe tener al menos 8 caracteres")
            }
        },
        textObfuscationMode = if (showPass) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        trailingIcon = {
            IconButton(onClick = { showPass = !showPass }) {
                Icon(
                    imageVector = if (showPass) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (showPass) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        }
    )
}