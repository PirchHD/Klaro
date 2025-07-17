package com.example.klaro.domain.repository.implementations

import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.ModelCollections
import com.example.klaro.domain.model.Promotion
import com.example.klaro.domain.repository.interfaces.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl  @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : MainRepository
{
    override suspend fun getOwnerFlashcardSets(): List<FlashcardSet> {
        val user = firebaseAuth.currentUser ?: return emptyList()
        return firestore
            .collection(ModelCollections.FLASHCARD_SETS)
            .whereEqualTo("ownerId", user.uid)
            .get()
            .await()
            .toObjects(FlashcardSet::class.java)
    }

    override suspend fun getFreeFlashcardSets(): List<FlashcardSet>
    {
        return firestore
            .collection(ModelCollections.FLASHCARD_SETS)
            .whereEqualTo("premium", false)
            .get()
            .await()
            .toObjects(FlashcardSet::class.java)
    }

    override suspend fun getCurrentPromotions(): List<Promotion>
    {
        val snapshot = firestore
            .collection("promotions")
            .get()
            .await()


        return TODO("Provide the return value")
    }


}
