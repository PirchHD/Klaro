package com.example.klaro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.klaro.ui.components.BottomNavBar
import com.example.klaro.ui.theme.KlaroTheme
import com.example.klaro.ui.viewmodels.ProfileViewModel

/**
 * Główny Composable dla ekranu profilu.
 * Pobiera ProfileViewModel przez hiltViewModel() i subskrybuje jego uiState.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // Obserwujemy stan z ViewModelu
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = state.userName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.onSettingsClick()
                        // Tutaj możesz np. navController.navigate("settings")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ustawienia",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        ProfileContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            userEmail = state.userEmail,
            cardsStudied = state.cardsStudied,
            streakDays = state.streakDays,
            recentActivities = state.recentActivities,
            onEditProfile = {
                viewModel.onEditProfile()
                // Tutaj np. navController.navigate("editProfile")
            },
            onSettingsClick = {
                viewModel.onSettingsClick()
                // Jeśli wolisz obsłużyć ustawienia ponownie, albo w tym miejscu:
                // navController.navigate("settings")
            }
        )
    }
}

/**
 * Zawartość ekranu profilu:
 * - wyświetla e-mail, statystyki i listę ostatnich aktywności
 * - przycisk „Edytuj profil”
 */
@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    userEmail: String,
    cardsStudied: Int,
    streakDays: Int,
    recentActivities: List<String>,
    onEditProfile: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Adres e-mail
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Statystyki: przerobione fiszki i streak
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = cardsStudied.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Fiszki",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = streakDays.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Dni z rzędu",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Przycisk edycji profilu
        Button(
            onClick = onEditProfile,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edytuj profil",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Edytuj profil")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Statystyki",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )


    }
}

/**
 * Preview pełnego ekranu ProfileScreen (jasny motyw).
 * Używamy ręcznie stworzonego Scaffolda i ProfileContent z przykładowymi danymi,
 * bo hiltViewModel() nie działa w preview bez konfiguracji Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenFullPreviewLight() {
    KlaroTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            text = "Jan Kowalski",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* symulacja ustawień */ }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ustawienia",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = { BottomNavBar(navController = navController) }
        ) { innerPadding ->
            ProfileContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                userEmail = "jan.kowalski@example.com",
                cardsStudied = 120,
                streakDays = 7,
                recentActivities = listOf(
                    "Ukończono zestaw: 'Niemiecki – Podstawy'",
                    "Rozpoczęto nowy zestaw: 'Słówka na egzamin'",
                    "Przegląd fiszek: 'Biologia – Komórki'",
                    "Nowa promocja: '-30% na abonament miesięczny'"
                ),
                onEditProfile = { /* symulacja kliknięcia */ },
                onSettingsClick = { /* symulacja kliknięcia */ }
            )
        }
    }
}

/**
 * Preview pełnego ekranu ProfileScreen (ciemny motyw).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
fun ProfileScreenFullPreviewDark() {
    KlaroTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            text = "Jan Kowalski",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* symulacja ustawień */ }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ustawienia",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = { BottomNavBar(navController = navController) }
        ) { innerPadding ->
            ProfileContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                userEmail = "jan.kowalski@example.com",
                cardsStudied = 120,
                streakDays = 7,
                recentActivities = listOf(
                    "Ukończono zestaw: 'Niemiecki – Podstawy'",
                    "Rozpoczęto nowy zestaw: 'Słówka na egzamin'",
                    "Przegląd fiszek: 'Biologia – Komórki'",
                    "Nowa promocja: '-30% na abonament miesięczny'"
                ),
                onEditProfile = { /* symulacja kliknięcia */ },
                onSettingsClick = { /* symulacja kliknięcia */ }
            )
        }
    }
}

/**
 * Preview tylko samego ProfileContent bez scaffoldu.
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileContentPreview() {
    KlaroTheme {
        ProfileContent(
            modifier = Modifier.fillMaxSize(),
            userEmail = "jan.kowalski@example.com",
            cardsStudied = 120,
            streakDays = 7,
            recentActivities = listOf(
                "Ukończono zestaw: 'Niemiecki – Podstawy'",
                "Rozpoczęto nowy zestaw: 'Słówka na egzamin'",
                "Przegląd fiszek: 'Biologia – Komórki'",
                "Nowa promocja: '-30% na abonament miesięczny'"
            ),
            onEditProfile = {},
            onSettingsClick = {}
        )
    }
}