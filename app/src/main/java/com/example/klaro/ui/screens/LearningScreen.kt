package com.example.klaro.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.klaro.R
import com.example.klaro.domain.model.flashcard.Card
import com.example.klaro.domain.model.review.LeitnerEntry
import com.example.klaro.domain.model.review.ReviewProgress
import com.example.klaro.domain.model.strategy.LeitnerStrategyConfig
import com.example.klaro.domain.model.strategy.StrategyConfig
import com.example.klaro.ui.components.KlaroTopAppBar
import com.example.klaro.ui.theme.KlaroTheme
import com.example.klaro.ui.viewmodels.LearningViewModel
import com.google.firebase.Timestamp


@Composable
fun LearningScreen(
    onBack: () -> Unit = {},
    viewModel: LearningViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            KlaroTopAppBar(
                title = state.flashcardSet?.title.orEmpty(),
                imageVector = Icons.Default.Close,
                onBack = onBack
            )
        },
        bottomBar = {
            ActionSection(
                onKnow = viewModel::onKnow,
                onDontKnow = viewModel::onDoNotKnow
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
        ) {
            if (state.isLoading || state.flashcardSet == null)
                CircularProgressIndicator()
            else
            {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                )
                {
                    BoxProgressRow(
                        strategy = state.strategy,
                        reviewProgress  = state.reviewProgress,
                        cardToLearnCount = (state.flashcardSet?.cards?.size ?: 0) - (state.reviewProgress.entries?.size ?: 0),
                        cardLearnedCount = state.cardLearnedCount,
                    )
                }


                val card = state.flashcardSet!!.cards[state.currentIndex]

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                )
                {
                    SwipeableFlashcard(
                        card = card,
                        isFlipped = state.isFlipped,
                        onFlip = viewModel::onFlip,
                        onSwipedRight = viewModel::onKnow,
                        onSwipedLeft = viewModel::onDoNotKnow
                    )
                }
            }
        }
    }
}

@Composable
fun BoxProgressRow(
    strategy: StrategyConfig,
    reviewProgress: ReviewProgress,
    modifier: Modifier = Modifier,
    cardToLearnCount: Int,
    cardLearnedCount: Int
) {
    val leitnerConfig = remember(strategy) { strategy as? LeitnerStrategyConfig } ?: return
    val entries = reviewProgress.entries.orEmpty().filterIsInstance<LeitnerEntry>()
    val countsByBox = entries.groupingBy { it.boxIndex }.eachCount()
    val boxes = leitnerConfig.boxConfigs.toSortedMap().map { (boxKey, cfg) -> Triple(cfg.name, countsByBox[boxKey] ?: 0, cfg.capacity) }

    val pairs = boxes.chunked(2)
    val firstPair = pairs.getOrNull(0).orEmpty()
    val secondPair = pairs.getOrNull(1).orEmpty()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoBox(
                title = "To learn",
                subtitle = cardToLearnCount.toString(),
                modifier = Modifier.weight(1f)
            )
            firstPair.forEach { (name, count, capacity) ->
                BoxLeitnerConfig(
                    name = name,
                    count = count,
                    capacity = capacity,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            secondPair.forEach { (name, count, capacity) ->
                BoxLeitnerConfig(
                    name = name,
                    count = count,
                    capacity = capacity,
                    modifier = Modifier.weight(1f)
                )
            }
            InfoBox(
                title = "Learned",
                subtitle = cardLearnedCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BoxLeitnerConfig(name: String, count: Int, capacity: Int, modifier: Modifier = Modifier)
{
    OutlinedCard(
        modifier = modifier
            .height(64.dp),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$count/${capacity}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun InfoBox(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .height(64.dp),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}


@Composable
fun SwipeableFlashcard(
    card: Card,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onSwipedRight: () -> Unit,
    onSwipedLeft: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(380.dp)
            .offset { IntOffset(offsetX.toInt(), 0) }
            .pointerInput(card.id) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    when {
                        offsetX > 200f -> { onSwipedRight(); offsetX = 0f }
                        offsetX < -200f -> { onSwipedLeft(); offsetX = 0f }
                    }
                }
            }
            .clickable { onFlip() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header: Category & Menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (!isFlipped) card.categoryFront else card.categoryBack,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { /* menu actions */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )

                // Main term
                Text(
                    text = if (!isFlipped) card.front else card.back,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Example usage
                Text(
                    text = if (!isFlipped) card.exampleOneFront else card.exampleOneBack,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Media icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { /* zdjęcie */ }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_photo_24),
                            contentDescription = "Zdjęcie"
                        )
                    }
                    IconButton(onClick = { /* głos */ }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_volume_up_24),
                            contentDescription = "Głos"
                        )
                    }
                    IconButton(onClick = { /* wideo */ }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_video_camera_back_24),
                            contentDescription = "Film"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionSection(
    onKnow: () -> Unit,
    onDontKnow: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            onClick = onDontKnow,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Nie znam")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onKnow,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Znam")
        }
    }
}

// ---------- PREVIEWS ----------

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LearningScreenPreview() {
    KlaroTheme {
        LearningScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxProgressRow() {

    MaterialTheme {
        BoxProgressRow(
            strategy = LeitnerStrategyConfig(),
            reviewProgress  = ReviewProgress(id = ""),
            cardToLearnCount = 1000,
            cardLearnedCount = 50
        )
    }
}

@Preview(showBackground = true, name = "One in Every Box")
@Composable
fun PreviewBoxProgressRowFull() {
    val now = Timestamp.now()
    MaterialTheme {
        BoxProgressRow(
            strategy = LeitnerStrategyConfig(),
            reviewProgress = ReviewProgress(
                userId = "user1",
                setId = "set1",
                entries = listOf(
                    LeitnerEntry("c1", now, boxIndex = 1),
                    LeitnerEntry("c2", now, boxIndex = 1),
                    LeitnerEntry("c3", now, boxIndex = 3),
                    LeitnerEntry("c4", now, boxIndex = 4),
                    LeitnerEntry("c5", now, boxIndex = 5)
                ),
                id = ""
            ),
            cardToLearnCount = 122,
            cardLearnedCount = 10
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SwipeableFlashcardFrontPreview() {
    KlaroTheme {
        SwipeableFlashcard(
            card = Card(
                id = "1",
                front = "Gruszka",
                back = "Pear",
                categoryFront = "Owoce",
                categoryBack = "Fruits",
                exampleOneFront = "Gruszka to owoc jadalny",
                exampleOneBack = "Pear is an edible fruit"
            ),
            isFlipped = false,
            onFlip = {},
            onSwipedRight = {},
            onSwipedLeft = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SwipeableFlashcardBackPreview() {
    KlaroTheme {
        SwipeableFlashcard(
            card = Card(
                id = "2",
                front = "Drzewo",
                back = "Tree",
                categoryFront = "Rośliny",
                categoryBack = "Plants",
                exampleOneFront = "Drzewo jest duże",
                exampleOneBack = "A tree is big"
            ),
            isFlipped = true,
            onFlip = {},
            onSwipedRight = {},
            onSwipedLeft = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActionSectionPreview() {
    KlaroTheme {
        ActionSection(onKnow = {}, onDontKnow = {})
    }
}
