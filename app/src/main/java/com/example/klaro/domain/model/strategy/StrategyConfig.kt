package com.example.klaro.domain.model.strategy

import androidx.annotation.Keep
import com.example.klaro.domain.model.review.ReviewMethod
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 */
sealed class StrategyConfig {
    open val setId: String? = null
    open val userId: String? = null
    abstract val method: ReviewMethod
}