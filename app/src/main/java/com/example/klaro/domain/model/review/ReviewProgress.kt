package com.example.klaro.domain.model.review

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Globalny kontener przechowujący postęp użytkownika dla konkretnego zestawu fiszek.
 *
 * @property userId unikalny identyfikator użytkownika
 * @property setId identyfikator zestawu fiszek
 * @property method wybrana metoda powtórek (enum ReviewMethod)
 * @property entries lista wpisów historii powtórek (ReviewEntry) dla każdej karty
 */
@IgnoreExtraProperties
@Keep
data class ReviewProgress(
    val id: String = "",
    val userId: String = "",
    val setId: String = "",
    val method: ReviewMethod = ReviewMethod.LEITNER,
    var entries: List<ReviewEntry>? = null,
)

/**
 * Wspólny interfejs reprezentujący pojedynczy wpis historii powtórek dla danej karty.
 * Polimorficzne podejście umożliwia przechowywanie różnych typów wpisów w jednej kolekcji.
 */
sealed interface ReviewEntry
{
    /** String: Unikalny identyfikator karty. */
    val cardId: String
    /** Timestamp: Data i godzina ostatniego przeglądu. */
    val lastReviewed: Timestamp?
}
