package com.example.klaro.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.klaro.ui.theme.KlaroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlaroTopAppBar(
    title: String,
    imageVector: ImageVector = Icons.Default.ArrowBack,
    onBack: (() -> Unit)?
) {
    TopAppBar(
        navigationIcon = {
            if (onBack != null)
            {
                IconButton(onClick = onBack) {
                        Icon(imageVector, contentDescription = "Back")
                }
            }
        },
        title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Preview()
@Composable
fun KlaroTopAppBarPreview() {
    KlaroTheme {
        KlaroTopAppBar("Koszyk", onBack = {})
    }
}

