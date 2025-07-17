package com.example.klaro.domain.model.review

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
@Keep
data class LeitnerEntry(
    override val cardId: String   = "",
    override val lastReviewed: Timestamp? = null,
    val boxIndex: Int = 1
) : ReviewEntry