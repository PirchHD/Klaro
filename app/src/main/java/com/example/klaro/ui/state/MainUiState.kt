package com.example.klaro.ui.state

import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.Promotion

/**
 * Stan głównego ekranu.
 *
 * @param todayCount (Int) liczba kart do powtórki dzisiaj.
 * @param ownerFlashcardSets (List<FlashcardSet>) lista dostępnych zestawów fiszek.
 * @param promotions (List<Promotion>) lista aktualnych promocji.
 */
data class MainUiState(
    val todayCount: Int = 0,
    val ownerFlashcardSets: List<FlashcardSet> = emptyList(),
    val freeFlashcardSets: List<FlashcardSet> = emptyList(),
    val promotions: List<Promotion> = emptyList()
) : UiState

