package com.example.klaro.navigation

sealed class Screen(val route: String)
{
    data object Start                : Screen("start")
    data object Login                : Screen("login")
    data object Registration         : Screen("registration")
    data object LanguageSelection    : Screen("language_selection")
    data object Main                 : Screen("main")
    data object Statistics           : Screen("statistics")
    data object Shop                 : Screen("shop")
    data object Profile              : Screen("profile")

    data object Learning : Screen("learning/{setId}") {
        fun createRoute(setId: String) = "learning/$setId"
    }


    data object FlashcardSetDetail : Screen("flashcard_set_detail/{setId}") {
        fun createRoute(setId: String) = "flashcard_set_detail/$setId"
    }
}
