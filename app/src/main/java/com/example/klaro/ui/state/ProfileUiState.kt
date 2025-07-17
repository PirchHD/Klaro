package com.example.klaro.ui.state

/**
 * Stan ekranu profilu użytkownika:
 * - userName: nazwa użytkownika (np. „Jan Kowalski”)
 * - userEmail: adres e-mail
 * - cardsStudied: liczba przerobionych fiszek
 * - streakDays: liczba dni z rzędu
 * - recentActivities: lista ostatnich aktywności (tekstowych opisów)
 */
data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val cardsStudied: Int = 0,
    val streakDays: Int = 0,
    val recentActivities: List<String> = emptyList()
)