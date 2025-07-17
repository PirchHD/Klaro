package com.example.klaro.domain.repository.implementations

import com.example.klaro.domain.model.flashcard.Card
import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.ModelCollections
import com.example.klaro.domain.model.review.LeitnerEntry
import com.example.klaro.domain.model.review.ReviewProgress
import com.example.klaro.domain.repository.interfaces.LearningRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningRepositoryImpl  @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : LearningRepository
{

    override suspend fun getFlashcardSetById(id: String): FlashcardSet
    {
        val doc = firestore
            .collection(ModelCollections.FLASHCARD_SETS)
            .document(id)
            .get()
            .await()

        val flashcardSet = doc.toObject(FlashcardSet::class.java) ?: throw IllegalStateException("Brak zestawu o id $id")

        val cardsSnapshot = firestore
            .collection(ModelCollections.FLASHCARD_SETS)
            .document(id)
            .collection(ModelCollections.CARDS)
            .get()
            .await()

        val cards = cardsSnapshot.toObjects(Card::class.java)

        return flashcardSet.copy(cards = cards)
    }

    override suspend fun getReviewProgress(setId: String): ReviewProgress?
    {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        val progressDocRef = firestore
            .collection(ModelCollections.USER_DATA)
            .document(userId)
            .collection(ModelCollections.REVIEW_PROGRESS)
            .document(setId)

        val docSnap = progressDocRef.get().await()

        return if (docSnap.exists())
        {
            val baseProg = docSnap.toObject(ReviewProgress::class.java)!!.copy(id = docSnap.id)

            val entriesSnap = progressDocRef
                .collection(ModelCollections.REVIEW_ENTRY)
                .get()
                .await()

            baseProg.copy(entries = entriesSnap.toObjects(LeitnerEntry::class.java))
        }
        else
        {
            val emptyProg = ReviewProgress(
                id      = setId,
                setId   = setId,
                entries = emptyList()
            )

            progressDocRef.set(emptyProg).await()
            emptyProg
        }
    }

    override suspend fun createOrSetReview(
        cardId: String,
        isKnow: Boolean,
        reviewProgressId: String
    ): ReviewProgress
    {
        val userId = auth.currentUser?.uid  ?: throw IllegalStateException("User not authenticated")

        val progressDocRef = firestore
            .collection(ModelCollections.USER_DATA)
            .document(userId)
            .collection(ModelCollections.REVIEW_PROGRESS)
            .document(reviewProgressId)

        val proSnap = progressDocRef.get().await()
        if (!proSnap.exists())
        {
            val emptyProb = ReviewProgress(
                id      = reviewProgressId,
                setId   = reviewProgressId,
                entries = emptyList()
            )

            progressDocRef.set(emptyProb).await()
        }

        val entryDocRef = progressDocRef
            .collection(ModelCollections.REVIEW_ENTRY)
            .document(cardId)

        val entrySnap = entryDocRef.get().await()

        if (isKnow)
        {
            if (entrySnap.exists())
            {
                val existing = entrySnap.toObject(LeitnerEntry::class.java)!!
                val incrementedBox = existing.boxIndex + 1

                entryDocRef.update("boxIndex", incrementedBox).await()
            }
            else
            {
                val newEntry = LeitnerEntry(
                    cardId = cardId,
                    boxIndex = 1
                )

                entryDocRef.set(newEntry).await()
            }
        }
        else
        {
            if (entrySnap.exists())
                entryDocRef.delete().await()
        }

        val updatedProgSnap = progressDocRef.get().await()
        val baseProg = updatedProgSnap
            .toObject(ReviewProgress::class.java)!!
            .copy(id = updatedProgSnap.id)

        val entriesSnap = progressDocRef
            .collection(ModelCollections.REVIEW_ENTRY)
            .get()
            .await()

        return baseProg.copy(entries = entriesSnap.toObjects(LeitnerEntry::class.java))
    }









}
