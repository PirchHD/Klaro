package com.example.klaro.domain.model

/**
 * Reprezentuje użytkownika w aplikacji.
 *
 * @param id (Long) identyfikator użytkownika.
 * @param name (String) imię użytkownika.
 * @param surname (String) nazwisko użytkownika.
 * @param address (String) adres użytkownika.
 * @param email (String) adres e-mail użytkownika.
 * @param password (String) hasło użytkownika.
 */
data class UserData(
    val id: Long,
    val name: String,
    val surname: String,
    val address: String,
    val email: String,
    val password: String
)
