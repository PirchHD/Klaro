package com.example.klaro.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klaro.domain.repository.interfaces.RegistrationRepository
import com.example.klaro.ui.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.klaro.domain.repository.Result

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repo: RegistrationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RegisterUiEvent>()
    val events: SharedFlow<RegisterUiEvent> = _events.asSharedFlow()

    /** Handlers for form field changes **/
    fun onFirstNameChange(value: String) = _uiState.update { it.copy(firstName = value) }
    fun onLastNameChange(value: String) = _uiState.update { it.copy(lastName = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onConfirmPasswordChange(value: String) = _uiState.update { it.copy(confirmPassword = value) }
    fun onBirthDateChange(value: String) = _uiState.update { it.copy(birthDate = value) }
    fun onGenderChange(value: String) = _uiState.update { it.copy(gender = value) }

    /** Attempts to register the user with email and password, validating inputs. **/
    fun register()
    {
        viewModelScope.launch {
            val state = _uiState.value
            when
            {
                state.firstName.isBlank() -> emitError("Podaj imię")
                state.lastName.isBlank() -> emitError("Podaj nazwisko")
                state.email.isBlank() -> emitError("Podaj email")
                state.password.isBlank() -> emitError("Podaj hasło")
                state.password != state.confirmPassword -> emitError("Hasła nie są zgodne")
            }

            viewModelScope.launch {
                val state = uiState.value
                _uiState.update { it.copy(isLoading = true) }
                when (val result = repo.registerWithEmail(
                    state.firstName, state.lastName,
                    state.email, state.password
                )) {
                    is Result.Success -> _events.emit(RegisterUiEvent.NavigateToMain)
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        emitError(result.message.toString())
                    }
                }
            }
        }
    }



    private suspend fun emitError(message: String)
    {
        _events.emit(RegisterUiEvent.ShowError(message))
    }
}
/**
 * One-time UI events from the registration ViewModel.
 */
sealed class RegisterUiEvent
{
    object NavigateToMain : RegisterUiEvent()
    data class ShowError(val message: String) : RegisterUiEvent()
}
