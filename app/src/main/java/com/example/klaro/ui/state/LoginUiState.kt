package com.example.klaro.ui.state

/**
 * Stan ekranu logowania.
 *
 * @param email                 - (String) aktualnie wpisywany e-mail.
 * @param password              - (String) aktualnie wpisywane hasło.
 * @param passwordVisible       - (Boolean) czy hasło jest widoczne.
 * @param isLoading             - (Boolean) czy trwa proces logowania.
 * @param validationLogin       - (String?) Informacja o walidacji loginu
 * @param validationPassword    - (String?) Informacja o walidacji hasła
 * @param errorMessage          - (String?) Krytyczny błąd
 */
data class LoginUiState(
    val email: String = "testuser@gmail.com", // TODO potem to usuń
    val password: String = "123456", // TODO potem to usuń
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val validationLogin: String? = null,
    val validationPassword: String? = null,
    val errorMessage: String? = null
) : UiState