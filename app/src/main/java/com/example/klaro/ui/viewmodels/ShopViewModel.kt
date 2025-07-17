package com.example.klaro.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.sampledata.SampleData
import com.example.klaro.ui.state.ShopUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel odpowiedzialne za logikę ekranu sklepu.
 * Pobiera dane (np. z repozytorium), przechowuje stan UI oraz
 * reaguje na akcje użytkownika (zmiana kategorii, zapytania wyszukiwania, zakup itp.).
 */
@HiltViewModel
class ShopViewModel @Inject constructor(
    // Jeżeli w przyszłości dodasz ShopRepository, wstrzyknij je tutaj:
    // private val repository: ShopRepository
) : ViewModel() {

    // MutableStateFlow przechowujące aktualny stan
    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    /**
     * Inicjalne załadowanie listy kategorii i pakietów premium.
     * Na razie korzystamy z SampleData, można to zastąpić wywołaniem do repozytorium.
     */
    private fun loadData() {
        viewModelScope.launch {
            // Pobranie wszystkich pakietów premium.
            // Jeśli w Twoim repozytorium jest np. repository.getPremiumFlashcardSets(), użyj tego.
            val allSets: List<FlashcardSet> = SampleData.cardFlashcardSets // ← zamień na źródło w produkcji

            // Wyodrębnienie unikalnych kategorii z pakietów:
            val categories = allSets.mapNotNull { it.category }.distinct()

            // Zaktualizowanie stanu:
            _uiState.update { current ->
                current.copy(
                    categories = categories,
                    selectedCategory = categories.firstOrNull().orEmpty(),
                    allPremiumFlashcardSets = allSets
                )
            }
        }
    }

    /** Wywoływane, gdy użytkownik wpisuje nową frazę w polu wyszukiwania */
    fun onSearchQueryChanged(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    /** Wywoływane, gdy użytkownik zmienia wybraną kategorię */
    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * Wywoływane, gdy użytkownik kliknie „Kup” przy danym pakiecie.
     * W tym miejscu można zainicjować dialog potwierdzenia, wywołać funkcję kupna w repozytorium itp.
     */
    fun onPurchaseClick(flashcardSet: FlashcardSet)
    {
        // TODO: tu możesz np. dodać logikę wywołania repozytorium kupna, a potem wysłać event do UI (np. otworzyć ekran koszyka).
    }
}