// ui/screens/ShopScreen.kt
package com.example.klaro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.ui.components.BottomNavBar
import com.example.klaro.ui.components.InputField
import com.example.klaro.ui.components.PrimaryButton
import com.example.klaro.ui.state.ShopUiState
import com.example.klaro.ui.viewmodels.ShopViewModel
import coil.compose.AsyncImage
import com.example.klaro.ui.theme.KlaroTheme

/**
 * Ekran sklepu z MVVM:
 * - pobiera ShopViewModel przez hiltViewModel()
 * - obserwuje stan uiState
 * - przekazuje akcje użytkownika z powrotem do viewModelu
 */
@Composable
fun ShopScreen(
    navController: NavHostController,
    viewModel: ShopViewModel = hiltViewModel()
) {
    // subskrybujemy stan UI z ViewModelu
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { ShopTopAppBar() },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        ShopContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            uiState = state,
            onCategoryClick = viewModel::onCategorySelected,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onPurchaseClick = viewModel::onPurchaseClick
        )
    }
}

@Composable
private fun ShopContent(
    modifier: Modifier = Modifier,
    uiState: ShopUiState,
    onCategoryClick: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onPurchaseClick: (FlashcardSet) -> Unit
) {
    Column(modifier = modifier) {
        // Pole wyszukiwania z przyciskiem filtra
        InputFieldSearch(
            searchQuery = uiState.searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onFilterClick = { /* opcjonalnie pokaż dialog wyboru kategorii */ }
        )

        // Lista pakietów premium (przefiltrowana)
        FlashcardList(
            premiumFlashcardSets = uiState.filteredFlashcardSets,
            onPurchaseClick = onPurchaseClick
        )
    }
}

/** Pole wyszukiwania z ikoną filtra */
@Composable
fun InputFieldSearch(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    InputField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        label = "Szukaj…",
        leadingIcon = Icons.Default.Search,
        trailingIcon = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = "Filtruj według kategorii",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}



/** Lista pakietów (przefiltrowana) */
@Composable
private fun FlashcardList(
    premiumFlashcardSets: List<FlashcardSet>,
    onPurchaseClick: (FlashcardSet) -> Unit
) {
    if (premiumFlashcardSets.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Brak pakietów dla wybranych kryteriów.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(premiumFlashcardSets, key = { it.id }) { item ->
                FlashcardItem(item = item, onPurchaseClick = onPurchaseClick)
            }
        }
    }
}

/** Pojedynczy element listy pakietów */
@Composable
private fun FlashcardItem(
    item: FlashcardSet,
    onPurchaseClick: (FlashcardSet) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!item.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            PrimaryButton(
                text = "${item.price} zł",
                onClick = { onPurchaseClick(item) },
                modifier = Modifier
                    .width(96.dp)
                    .height(36.dp)
            )
        }
    }
}

/**
 * Pasek górny dla ekranu sklepu:
 * - tytuł „Sklep”
 * - ikona koszyka (można podpiąć akcję)
 */
@Composable
fun ShopTopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Sklep",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = { /* np. nawigacja do koszyka */ }) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "Przejdź do koszyka",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/**
 * Preview całego ekranu ShopScreen (jasny motyw)
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ShopScreenFullPreviewLight() {
    KlaroTheme {
        val navController = rememberNavController()
        ShopScreen(navController = navController)
    }
}

/**
 * Preview całego ekranu ShopScreen (ciemny motyw)
 */
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
fun ShopScreenFullPreviewDark() {
    KlaroTheme {
        val navController = rememberNavController()
        ShopScreen(navController = navController)
    }
}