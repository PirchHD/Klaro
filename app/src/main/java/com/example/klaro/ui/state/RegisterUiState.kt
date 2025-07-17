package com.example.klaro.ui.state

/**
 * Stan ekranu rejestracji.
 *
 *
 */
data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val isLoading: Boolean = false
) : UiState

