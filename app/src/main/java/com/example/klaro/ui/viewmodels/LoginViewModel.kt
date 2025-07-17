package com.example.klaro.ui.viewmodels

import android.content.Intent
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klaro.domain.repository.interfaces.LoginRepository
import com.example.klaro.ui.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.klaro.domain.repository.Result
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: LoginRepository, private val googleSignInClient: GoogleSignInClient ) : ViewModel()
{

    private val _uiState = MutableStateFlow(LoginUiState())

    /** Strumień stanu UI dla ekranu logowania. */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<LoginUiEvent>(Channel.BUFFERED)

    /** Strumień zdarzeń UI (nawigacja, komunikaty o błędach itp.). */
    val events = _eventChannel.receiveAsFlow()

    /**
     * Aktualizuje wartość pola e-mail w stanie UI.
     * Resetuje również ewentualny komunikat o błędzie.
     *
     * @param newEmail wprowadzony przez użytkownika adres e-mail
     */
    fun onEmailChange(newEmail: String)
    {
        _uiState.update { it.copy(email = newEmail, errorMessage = null) }
    }

    /**
     * Aktualizuje wartość pola hasła w stanie UI.
     * Resetuje również ewentualny komunikat o błędzie.
     *
     * @param newPassword wprowadzone przez użytkownika hasło
     */
    fun onPasswordChange(newPassword: String)
    {
        _uiState.update { it.copy(password = newPassword, errorMessage = null) }
    }

    /**
     * Przełącza widoczność wprowadzanego hasła.
     */
    fun onTogglePasswordVisibility()
    {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    /**
     * Próbuje zalogować użytkownika przy użyciu adresu e-mail i hasła.
     * Wykonuje wstępną walidację danych, a następnie wywołuje metodę repozytorium.
     *
     * @param email     - (String) nieprzetworzony adres e-mail
     * @param password  - (String) nieprzetworzone hasło
     */
    fun login(email: String, password: String)
    {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val trimmedEmail = email.trim()

            if (trimmedEmail.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches())
            {
                _uiState.update { it.copy(isLoading = false, validationLogin = "Niepoprawny adres e-mail") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = false, validationLogin = null) }

            if (password.length < 6)
            {
                _uiState.update { it.copy(isLoading = false, validationPassword = "Hasło musi mieć min. 6 znaków") }
                return@launch
            }

            when (val result = repo.login(trimmedEmail, password)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(LoginUiEvent.NavigateToMain)
                }
                is Result.Error   -> {
                    val msg = result.message
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                    _eventChannel.send(LoginUiEvent.ShowError(msg))
                }

            }
        }
    }

    /**
     * Inicjuje proces logowania przez Google.
     * Generuje Intent i wysyła zdarzenie z obiektem Intent.
     */
    fun startGoogleSignIn() = viewModelScope.launch {
        val signInIntent = googleSignInClient.signInIntent
        _eventChannel.send(LoginUiEvent.GoogleSignInIntent(signInIntent))
    }

    /**
     * Loguje użytkownika przy użyciu tokena Google otrzymanego z UI.
     * Wysyła zdarzenia nawigacyjne lub komunikaty o błędzie.
     *
     * @param idToken token ID Google
     */
    fun loginWithGoogle(idToken: String) = viewModelScope.launch {

        _uiState.update { it.copy(isLoading = true) }
        when(val res = repo.loginWithGoogle(idToken))
        {
            is Result.Success<*> ->
            {
                _uiState.update { it.copy(isLoading = false) }
                _eventChannel.send(LoginUiEvent.NavigateToMain)
            }

            is Result.Error ->
            {
                _uiState.update { it.copy(isLoading = false, errorMessage = res.message) }
                _eventChannel.send(LoginUiEvent.ShowError(res.message))
            }
        }
    }
}

sealed class LoginUiEvent
{
    object NavigateToMain : LoginUiEvent()
    data class GoogleSignInIntent(val intent: Intent): LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}
