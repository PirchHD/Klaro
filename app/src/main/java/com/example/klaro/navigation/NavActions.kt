package com.example.klaro.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController)
{

    val navigateToStart: () -> Unit = { navController.navigate(Screen.Start.route) }

    val navigateToLogin: () -> Unit = { navController.navigate(Screen.Login.route) }

    val navigateToRegistration: () -> Unit = { navController.navigate(Screen.Registration.route) }

    val navigateToLanguageSelection: () -> Unit = { navController.navigate(Screen.LanguageSelection.route) }

    val navigateToMain: () -> Unit = { navController.navigate(Screen.Main.route) }

    val navigateToStatistics: () -> Unit = { navController.navigate(Screen.Statistics.route) }

    val navigateToShop: () -> Unit = { navController.navigate(Screen.Shop.route) }

    val navigateToProfile: () -> Unit = { navController.navigate(Screen.Profile.route) }

    fun navigateToFlashcardDetail(setId: String) {
        navController.navigate(Screen.FlashcardSetDetail.createRoute(setId))
    }

    fun goBack() = navController.popBackStack()
}
