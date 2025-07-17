package com.example.klaro.domain.model.strategy

import androidx.annotation.Keep
import com.example.klaro.domain.model.review.ReviewMethod
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 */
@IgnoreExtraProperties
@Keep
data class LeitnerStrategyConfig @JvmOverloads constructor
(
    override val method: ReviewMethod = ReviewMethod.LEITNER,
    val boxCount: Int = 3,
    val boxConfigs: Map<Int, BoxConfig> = mapOf(
        1 to BoxConfig(name = "Box 1"),
        2 to BoxConfig(capacity = 100, name = "Box 2"),
        3 to BoxConfig(capacity = 300, name = "Box 3"),
        4 to BoxConfig(capacity = 600, name = "Box 4"),
    )
) : StrategyConfig()

/**
 */
@IgnoreExtraProperties
@Keep
data class BoxConfig(
    val capacity: Int = 50,
    val name: String = ""
)