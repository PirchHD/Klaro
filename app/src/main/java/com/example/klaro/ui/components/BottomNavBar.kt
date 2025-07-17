package com.example.klaro.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.klaro.navigation.NavActions
import com.example.klaro.navigation.Screen
import com.example.klaro.ui.theme.KlaroTheme

sealed class BottomNavItem(
    val route: String,
    val titleRes: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(Screen.Main.route, "Home", Icons.Default.Home)
    data object Shop : BottomNavItem(Screen.Shop.route,"Shop", Icons.Default.ShoppingCart)
    data object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}

@Composable
fun BottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navActions = remember(navController) { NavActions(navController) }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Shop,
        BottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    when (item) {
                        is BottomNavItem.Home    -> navActions.navigateToMain()
                        is BottomNavItem.Shop    -> navActions.navigateToShop()
                        is BottomNavItem.Profile -> navActions.navigateToProfile()
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.titleRes) },
                label = { Text(item.titleRes) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}

@Preview()
@Composable
fun BottomNavBarPreview() {
    KlaroTheme {
        val navController = rememberNavController()
        BottomNavBar(navController)
    }
}

