package com.example.klaro.ui.state

import com.example.klaro.domain.model.flashcard.FlashcardSet

/**
 * Stan ekranu sklepu:
 * - lista kategorii,
 * - wybrana kategoria,
 * - zapytanie wyszukiwania,
 * - lista wszystkich pakietów premium,
 * - flitracja listy w czasie rzeczywistym.
 */
data class ShopUiState(
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "",
    val searchQuery: String = "",
    val allPremiumFlashcardSets: List<FlashcardSet> = emptyList()
) {
    /**
     * Zwraca przefiltrowaną listę pakietów premium,
     * biorąc pod uwagę aktualną kategorię i frazę wyszukiwania.
     */
    val filteredFlashcardSets: List<FlashcardSet>
        get() = allPremiumFlashcardSets
            .filter { it.category == selectedCategory || selectedCategory.isBlank() }
            .filter { set ->
                searchQuery.isBlank() ||
                        set.title.contains(searchQuery, ignoreCase = true) ||
                        (set.description?.contains(searchQuery, ignoreCase = true) ?: false)
            }
}


