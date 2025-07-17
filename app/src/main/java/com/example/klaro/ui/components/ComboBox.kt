package com.example.klaro.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Uniwersalny ComboBox dla dowolnego typu T.
 *
 * @param options           - Lista opcji do wyświetlenia.
 * @param selectedOption    - Aktualnie wybrana opcja lub null.
 * @param onOptionSelected  - Callback wywoływany po wyborze opcji.
 * @param label             - Etykieta pola.
 * @param modifier          - Modyfikator Composable.
 * @param optionLabel       - Funkcja mapująca element T na String do wyświetlenia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ComboBox(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    optionLabel: (T) -> String = { it.toString() }
) {
    val expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 1.dp),
    ) {
        OutlinedTextField(
            value = selectedOption?.let(optionLabel) ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor       = MaterialTheme.colorScheme.surface,
                focusedBorderColor   = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor          = MaterialTheme.colorScheme.primary
            )
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem (
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ComboBoxPreview()
{
    val items = listOf("Opcja 1", "Opcja 2", "Opcja 3")
    val selected = remember { mutableStateOf<String?>(null) }
    ComboBox(
        options = items,
        selectedOption = selected.value,
        onOptionSelected = { selected.value = it },
        label = "Wybierz opcję",
        optionLabel = { it }
    )
}