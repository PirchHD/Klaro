package com.example.klaro.domain.model.sampledata

import com.example.klaro.domain.model.flashcard.Card
import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.Promotion
import com.example.klaro.domain.model.UserData

/**
 * Przykładowe dane domenowe do podglądów i testów.
 */
object SampleData {
    val sampleUser = UserData(
        id = 1L,
        name = "Jan",
        surname = "Kowalski",
        address = "Warszawa",
        email = "jan.kowalski@example.com",
        password = "password"
    )

    val cardFlashcardSets = listOf(

        FlashcardSet(
            id = "1",
            title = "Język Angielski A1",
            description = "Podstawowe konstrukcje",
            cards = listOf(
                Card("1-1", "Dog", "Pies", 2, "test", ""),
                Card("1-2", "Cat", "Kot", 1, "test", "")
            ),
            imageUrl = "",
            price = 3.99,
            category = "Angielski"
        ),

        FlashcardSet(
            id = "2",
            title = "Język Angielski - czasowiniki",
            description = "Najważniejsze czasowniki",
            cards = listOf(
                Card("2-1", "to be", "być", 2, "test", ""),
                Card("2-2", "to have", "mieć", 1, "test", "")
            ),
            imageUrl = "",
            price = 5.99,
            category = "Angielski"
        )
    )

    val promotions = listOf(
        Promotion("promo1", "Zestaw Fiszki IT", "-20% na pakiet", null),
        Promotion("promo2", "Fiszki Językowe", "-30% na zestaw angielski", null)
    )
}