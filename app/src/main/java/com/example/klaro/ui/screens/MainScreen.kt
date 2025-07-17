package com.example.klaro.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.klaro.domain.model.Promotion
import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.sampledata.SampleData
import com.example.klaro.navigation.Screen
import com.example.klaro.ui.components.BottomNavBar
import com.example.klaro.ui.theme.KlaroTheme
import com.example.klaro.ui.viewmodels.MainUiEvent
import com.example.klaro.ui.viewmodels.MainViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()
    remember { SnackbarHostState() }

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is MainUiEvent.NavigateToReview -> {
                    //navController.navigate(Screen.Review.route)
                }
                is MainUiEvent.NavigateToCreateSet -> {
                    navController.navigate(Screen.Shop.route)
                }
                is MainUiEvent.NavigateToStats -> {
                    navController.navigate(Screen.Statistics.route)
                }
                is MainUiEvent.NavigateToNotifications -> {
                   // navController.navigate(Screen.Notifications.route)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            KMainTopBar(
                userName = "Szymon",
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onNotificationsClick = viewModel::onNotificationsClick)},
        floatingActionButton = { KCreateSetFab(onClick = viewModel::onCreateSetClick) },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        KContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            todayCount     = state.todayCount,
            ownerFlashcardSets  = state.ownerFlashcardSets,
            freeFlashcardSets  = state.freeFlashcardSets,
            promotions     = state.promotions,
            onReviewClick  = viewModel::onReviewClick,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KMainTopBar(
    userName: String,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profil",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        title = {
            Text(
                text = "Cześć, $userName !",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Powiadomienia",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun KCreateSetFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Dodaj zestaw")
    }
}

@Composable
private fun KContent(
    modifier: Modifier = Modifier,
    todayCount: Int,
    ownerFlashcardSets: List<FlashcardSet>,
    freeFlashcardSets: List<FlashcardSet>,
    promotions: List<Promotion>,
    onReviewClick: () -> Unit,
    navController: NavHostController
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item { KTodayCard(todayCount, onReviewClick) }
        item { KPromoSection(promotions) }
        item { KSectionTitle("Twoje zestawy") }
        items(ownerFlashcardSets, key = { "owner-${it.id}" }) { set -> KFlashcardSetItem(set, navController) }
        item { KSectionTitle("Darmowe zestawy") }
        items( freeFlashcardSets, key = { "free-${it.id}" }) { set -> KFlashcardSetItem(set, navController) }
    }
}

@Composable
private fun KSectionTitle(text: String)
{
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun KPromoSection(promotions: List<Promotion>)
{
    if (promotions.isEmpty()) return

    var currentIndex by remember { mutableStateOf(0) }

    // co 5 sekund przełącz kolejną promocję
    LaunchedEffect(promotions.size) {
        while (true) {
            kotlinx.coroutines.delay(5000)
            currentIndex = (currentIndex + 1) % promotions.size
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        KPromoItem(promotions[currentIndex])
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KPromoItem(promo: Promotion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = { /* tutaj np. przejdź do szczegółów promocji */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            promo.thumbnail?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = promo.discount,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = promo.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun KTodayCard(todayCount: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$todayCount fiszek do powtórki",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Dzisiaj",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Rozpocznij", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun KFlashcardSetItem(flashcardSet: FlashcardSet, navController: NavHostController)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//                Image(
//                    //painter = painterResource(id = set.imageUrl),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(48.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = flashcardSet.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${flashcardSet.cards.size} fiszek - 0%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            IconButton(onClick = {
                navController.navigate(Screen.Learning.createRoute(flashcardSet.id))
            }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    KlaroTheme {
        val navController = rememberNavController()
        MainScreen(
            navController = navController
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KContentPreview() {
    val navController = rememberNavController()

    KlaroTheme {
        KContent(
            modifier = Modifier.fillMaxSize(),
            todayCount = 7,
            ownerFlashcardSets = SampleData.cardFlashcardSets,
            freeFlashcardSets = SampleData.cardFlashcardSets,
            promotions = SampleData.promotions,
            onReviewClick = {},
            navController = navController
        )
    }
}


@Preview(showBackground = true)
@Composable
fun KMainTopBarPreview() {
    KlaroTheme {
        KMainTopBar(
            userName = "Anna",
            onProfileClick = {},
            onNotificationsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KPromoSectionPreview() {
    KlaroTheme {
        KPromoSection(promotions = SampleData.promotions)
    }
}

@Preview(showBackground = true)
@Composable
fun KTodayCardPreview() {
    KlaroTheme {
        KTodayCard(todayCount = 7, onClick = {})
    }
}
