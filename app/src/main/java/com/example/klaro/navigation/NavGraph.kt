package com.example.klaro.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.klaro.domain.model.sampledata.SampleData
import com.example.klaro.ui.screens.*
import com.example.klaro.ui.viewmodels.MainViewModel


@Composable
fun NavGraph(navController: NavHostController)
{

    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {

        composable(Screen.Start.route) {
            StartScreen(onStartClick = { navController.navigate(Screen.Login.route) })
        }

        composable(Screen.LanguageSelection.route) {
            LanguageSelectionScreen(
                onNativeLanguageSelected = { /* zapisz */ },
                onLearnLanguageToggled = { /* zapisz */ }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToMain = {navController.navigate(Screen.Main.route)},
                onNavigateToRegister = {navController.navigate(Screen.Registration.route)}
            )
        }

        composable(Screen.Registration.route) {
            RegistrationScreen(
                onGoogleRegister = { /* Google OAuth */ },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }


        composable(Screen.Main.route) { MainScreen(navController = navController)}

        composable(Screen.Shop.route) {ShopScreen(navController = navController) }

        composable(Screen.Profile.route) { ProfileScreen(navController = navController) }

        composable(
            route = Screen.FlashcardSetDetail.route,
            arguments = listOf(navArgument("setId") { type = NavType.StringType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId")!!
            RegistrationScreen(
                onGoogleRegister = { /* Google OAuth */ },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Learning.route,
            arguments = listOf(navArgument("setId") { type = NavType.StringType })
        ) { backStack ->
            LearningScreen(onBack = { navController.popBackStack() })
        }
    }
}