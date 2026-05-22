package com.example.proyecto.composables

import android.util.Patterns.EMAIL_ADDRESS
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlin.text.matches

@Composable
fun OutlinedEmailTextField(
    state: TextFieldState,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    showError: Boolean = false,
    isError: Boolean = false,
    onValidationChange: (isError: Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var correoValido by remember { mutableStateOf(false) }
    // Observa cambios en el texto y notifica al padre
    LaunchedEffect(Unit) {
        snapshotFlow { state.text.toString() }
            .collect { text ->
                correoValido = text.isNotEmpty() && EMAIL_ADDRESS.matcher(text).matches()
                onValidationChange(!correoValido)
            }
    }
    OutlinedTextField(
        state = state,
        modifier = modifier,
        label = label ?: { Text("Correo electrónico") },
        trailingIcon = trailingIcon,
        isError = showError && (!correoValido || isError), //en caso de error se vuelve rojo
        supportingText = {
            if (showError && !correoValido) {
                Text("Correo inválido")
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        )
    )
}

/*
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.5.0-alpha11")
    tengo q meter esto
    */