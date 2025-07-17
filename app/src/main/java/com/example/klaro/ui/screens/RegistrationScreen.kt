package com.example.klaro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.klaro.ui.theme.KlaroTheme
import com.example.klaro.ui.viewmodels.RegisterUiEvent
import com.example.klaro.ui.viewmodels.RegistrationViewModel
import androidx.compose.material3.IconButton
import com.example.klaro.ui.components.ComboBox
import com.example.klaro.ui.components.DateField
import com.example.klaro.ui.components.InputField
import com.example.klaro.ui.components.PrimaryButton
import com.example.klaro.ui.components.SecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onNavigateToLogin: () -> Unit,
    onGoogleRegister: () -> Unit
) {
    val viewModel: RegistrationViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterUiEvent.NavigateToMain -> {
                    onNavigateToLogin() // or navigate to main screen
                }
                is RegisterUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Rejestracja",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                InputField(
                    value = uiState.firstName,
                    onValueChange = viewModel::onFirstNameChange,
                    label = "Imię",
                    leadingIcon = Icons.Default.Person
                )

                InputField(
                    value = uiState.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    label = "Nazwisko",
                    leadingIcon = Icons.Default.Person
                )

                InputField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email",
                    leadingIcon = Icons.Default.Email
                )

                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                InputField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Hasło",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ukryj hasło" else "Pokaż hasło"
                            )
                        }
                    }
                )

                InputField(
                    value = uiState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Potwierdź hasło",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ukryj hasło" else "Pokaż hasło"
                            )
                        }
                    }
                )

                DateField(label = "Data urodzenia", value = uiState.birthDate, onValueChange = viewModel::onBirthDateChange)

                val genderOptions = listOf("Kobieta", "Mężczyzna", "Inna", "Nie chcę podać")
                ComboBox(
                    options = genderOptions,
                    selectedOption   = uiState.gender,
                    onOptionSelected = viewModel::onGenderChange,
                    label            = "Płeć"
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(text = "Zarejestruj się", onClick = viewModel::register)

                Spacer(modifier = Modifier.height(16.dp))

                SecondaryButton(text = "Zarejestruj się przez Google", onClick = onGoogleRegister)

                Row(
                    modifier = Modifier.padding(top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Masz już konto?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Zaloguj się",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationScreen() {
    KlaroTheme {
        RegistrationScreen({}, {})
    }
}
