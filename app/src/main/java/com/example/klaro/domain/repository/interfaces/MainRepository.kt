package com.example.klaro.domain.repository.interfaces

import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.Promotion

interface MainRepository
{
    suspend fun getOwnerFlashcardSets(): List<FlashcardSet>
    suspend fun getFreeFlashcardSets(): List<FlashcardSet>
    suspend fun getCurrentPromotions(): List<Promotion>
}