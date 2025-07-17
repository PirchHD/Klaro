package com.example.klaro.domain.model

/**
 * Model domenowy bannera promocyjnego.
 *
 * @param id (String) unikalny identyfikator bannera.
 * @param title (String) tytuł bannera.
 * @param discount (String) opis zniżki (np. "-20%").
 * @param thumbnail (Int?) identyfikator zasobu grafiki bannera.
 */
data class Promotion(
    val id: String,
    val title: String,
    val discount: String,
    val thumbnail: Int? = null
)

