package com.example.klaro.domain.model.flashcard

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Model domenowy zestawu fiszek.
 *
 * @param id            - (String) unikalny identyfikator zestawu.
 * @param title         - (String) tytuł zestawu.
 * @param description   - (String) opis zestawu.
 * @param cards         - (List<Card>) lista fiszek w zestawie.
 * @param imageUrl      - (String) URL miniaturki zestawu.
 * @param price         - (String?) cena zestawu.
 * @param category      - (String?) kategoria zestawu.
 * @param premium     - (boolean) Czy zestaw jest premium i wymaga kupna ?
 * @param ownerId       - (String) Identyfikator właściciela (Uzupełnione gdy to jest jego własna zrobiony zestaw)
 */
@IgnoreExtraProperties
@Keep
data class FlashcardSet @JvmOverloads constructor(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var cards: List<Card> = emptyList(),
    var imageUrl: String = "",
    var price: Double? = null,
    var category: String? = null,
    var premium: Boolean = false,
    var ownerId: String? = null
)