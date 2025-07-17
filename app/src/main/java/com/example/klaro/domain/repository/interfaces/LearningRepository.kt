package com.example.klaro.domain.repository.interfaces

import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.review.ReviewProgress

interface LearningRepository
{
    suspend fun getFlashcardSetById(id: String): FlashcardSet

    suspend fun getReviewProgress(setId: String): ReviewProgress?

    suspend fun createOrSetReview(cardId: String, isKnow: Boolean, reviewProgressId: String): ReviewProgress

}