package com.example.klaro.domain.model.review


/**
 * Definiuje dostępne algorytmy powtórek fiszek.
 */
enum class ReviewMethod
{
    /**
     * Klasyczny system „skrzynkowy” Leitnera:
     * – każda poprawna odpowiedź przesuwa kartę do kolejnej, rzadszej skrzynki;
     * – każda błędna – wraca na początek.
     */
    LEITNER,

    /**
     * Algorytm SM-2 (SuperMemo 2):
     * – powtórki w oparciu o ocenę jakości odpowiedzi (0–5);
     * – odstępy rosną wykładniczo.
     */
    SPACED_REPETITION_SM2,
}