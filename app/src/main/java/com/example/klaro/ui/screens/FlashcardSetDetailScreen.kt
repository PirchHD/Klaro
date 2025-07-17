package com.example.klaro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.ui.theme.KlaroTheme
import coil.compose.AsyncImage
import com.example.klaro.domain.model.sampledata.SampleData
import com.example.klaro.ui.components.KlaroTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardSetDetailScreen(
    flashcardSet: FlashcardSet,
    onBack: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    addToCart: (String) -> Unit
) {
    Scaffold(
        topBar = { KlaroTopAppBar(title = flashcardSet.title, onBack = onBack) },
        bottomBar = {
            OutlinedButton(
                onClick = { addToCart },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text("Kontynuuj", style = MaterialTheme.typography.labelLarge)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Image header
            AsyncImage(
                model = flashcardSet.imageUrl,
                contentDescription = "Zdjęcie zestawu fiszek",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Basic info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    onClick = { /* tag action */ },
                    label = { Text(text = "Zestaw edukacyjny") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "10 fiszek",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Expandable sections
            var descExpanded by remember { mutableStateOf(true) }
            SectionCard(
                title = "Opis",
                expanded = descExpanded,
                onToggle = { descExpanded = !descExpanded }
            ) {
                Text(
                    text = flashcardSet.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            var sampleExpanded by remember { mutableStateOf(false) }
            SectionCard(
                title = "Przykładowe fiszki",
                expanded = sampleExpanded,
                onToggle = { sampleExpanded = !sampleExpanded }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "1. Bonjour – Dzień dobry")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "2. Merci – Dziękuję")
                }
            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(
                        if (expanded) android.R.drawable.arrow_up_float else android.R.drawable.arrow_down_float
                    ),
                    contentDescription = null
                )
            }
            if (expanded) content()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FlashcardSetDetailScreenPreview() {

    KlaroTheme {
        FlashcardSetDetailScreen(
            flashcardSet = SampleData.cardFlashcardSets.first(),
            onBack = {},
            onToggleFavorite = {},
            addToCart = {}
        )
    }
}
