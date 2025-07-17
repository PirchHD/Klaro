package com.example.klaro.domain.model.flashcard

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Model pojedynczej fiszki.
 *
 * @param id (String) unikalny identyfikator fiszki.
 * @param front (String) zawartość pierwszej strony fiszki.
 * @param back (String) zawartość drugiej strony fiszki.
 */
@IgnoreExtraProperties
@Keep
data class Card @JvmOverloads constructor
(
    val id: String = "",
    val front: String = "",
    val back: String = "",
    val lp: Int = 0,
    val categoryFront: String = "",
    val categoryBack: String = "",
    val exampleOneFront: String = "",
    val exampleOneBack: String = "",
    val imageUrl: String = ""
)