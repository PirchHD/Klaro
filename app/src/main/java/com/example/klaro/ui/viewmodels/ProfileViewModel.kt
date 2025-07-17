// ui/viewmodels/ProfileViewModel.kt
package com.example.klaro.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klaro.ui.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel odpowiedzialne za logikę ekranu profilu:
 * - utrzymuje ProfileUiState
 * - na starcie ładuje dane użytkownika (tutaj przykładowo)
 * - można tutaj umieścić metody do edycji profilu czy otwierania ustawień
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    // W przyszłości możesz wstrzyknąć tutaj np. UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Na razie wypełniamy stan przykładowymi danymi.
     * W production możesz np. wywołać repository.getUserProfile()
     * i zaktualizować _uiState.
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            // Przykładowe dane (zastąp rzeczywistym wywołaniem repozytorium):
            val name = "Jan Kowalski"
            val email = "jan.kowalski@example.com"
            val cardsStudied = 120
            val streakDays = 7
            val activities = listOf(
                "Ukończono zestaw: 'Niemiecki – Podstawy'",
                "Rozpoczęto nowy zestaw: 'Słówka na egzamin'",
                "Przegląd fiszek: 'Biologia – Komórki'",
                "Nowa promocja: '-30% na abonament miesięczny'"
            )

            _uiState.update {
                it.copy(
                    userName = name,
                    userEmail = email,
                    cardsStudied = cardsStudied,
                    streakDays = streakDays,
                    recentActivities = activities
                )
            }
        }
    }

    /** Możesz tu dodać logikę przed nawigacją do edycji profilu */
    fun onEditProfile() {
        // np. przygotowanie stanu przed przejściem do ekranu edycji
    }

    /** Możesz tu dodać logikę przed nawigacją do ustawień */
    fun onSettingsClick() {
        // np. przygotowanie stanu przed otwarciem ustawień
    }
}