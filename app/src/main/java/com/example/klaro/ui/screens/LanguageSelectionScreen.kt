package com.example.klaro.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.klaro.R
import com.example.klaro.ui.theme.KlaroTheme
import androidx.compose.material3.OutlinedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(
    availableLanguages: List<Language> = listOf(
        Language("Polski", R.drawable.pl),
        Language("Angielski", R.drawable.pl),
        Language("Niemiecki", R.drawable.pl),
        Language("Francuski", R.drawable.pl),
        Language("Hiszpański", R.drawable.pl),
        Language("Włoski", R.drawable.pl),
        Language("Rosyjski", R.drawable.pl),
        Language("Chiński", R.drawable.pl),
        Language("Japoński", R.drawable.pl),
        Language("Arabski", R.drawable.pl)
    ),
    onNativeLanguageSelected: (String) -> Unit = {},
    onLearnLanguageToggled: (String) -> Unit = {}
) {
    var nativeExpanded by remember { mutableStateOf(false) }
    var nativeLanguage by remember { mutableStateOf(availableLanguages.first()) }
    var selectedLearnLanguages by remember { mutableStateOf(setOf<Language>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        Text(text = "Język ojczysty",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = nativeExpanded,
            onExpandedChange = { nativeExpanded = !nativeExpanded }
        ) {
            OutlinedTextField(
                value = nativeLanguage.name,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = nativeLanguage.flag),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Wybierz język ojczysty") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = nativeExpanded) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )


            ExposedDropdownMenu(
                expanded = nativeExpanded,
                onDismissRequest = { nativeExpanded = false }
            ) {
                availableLanguages.forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = language.flag),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp)
                                )
                                Text(language.name)
                            }
                        },
                        onClick = {
                            nativeLanguage = language
                            onNativeLanguageSelected(language.name)
                            nativeExpanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Języki do nauki",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(availableLanguages) { language ->
                val isSelected = selectedLearnLanguages.contains(language)
                Card(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable {
                            selectedLearnLanguages =
                                if (isSelected) selectedLearnLanguages - language else selectedLearnLanguages + language
                            onLearnLanguageToggled(language.name)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = language.flag),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Text(
                            text = language.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        OutlinedButton(
            onClick = { /** TO DO */},
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text("Kontynuuj", style = MaterialTheme.typography.labelLarge)
        }

    }

}

/**
 * Model języka z nazwą i zasobem flagi.
 */
data class Language(
    val name: String,
    @DrawableRes val flag: Int
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LanguageSelectionScreenPreview() {
    KlaroTheme {
        LanguageSelectionScreen()
    }
}
