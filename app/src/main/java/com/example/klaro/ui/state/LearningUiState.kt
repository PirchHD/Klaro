package com.example.klaro.ui.state

import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.review.ReviewProgress
import com.example.klaro.domain.model.strategy.LeitnerStrategyConfig
import com.example.klaro.domain.model.strategy.StrategyConfig

data class LearningUiState(
    val flashcardSet: FlashcardSet? = null,
    val strategy: StrategyConfig = LeitnerStrategyConfig(),
    val reviewProgress: ReviewProgress = ReviewProgress(),
    val cardLearnedCount: Int = 0,
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true
) : UiState

