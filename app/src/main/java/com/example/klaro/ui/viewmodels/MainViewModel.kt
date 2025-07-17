package com.example.klaro.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klaro.domain.repository.interfaces.MainRepository
import com.example.klaro.ui.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    private val _eventChannel = Channel<MainUiEvent>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
//            val todayCount = mainRepository.getTodayCount()
            _uiState.value = MainUiState(
                todayCount = 100,
                ownerFlashcardSets = mainRepository.getOwnerFlashcardSets(),
                freeFlashcardSets = mainRepository.getFreeFlashcardSets()
               // promotions = mainRepository.getCurrentPromotions()
            )
        }
    }

    fun onReviewClick() = viewModelScope.launch {
        _eventChannel.send(MainUiEvent.NavigateToReview)
    }

    fun onCreateSetClick() = viewModelScope.launch {
        _eventChannel.send(MainUiEvent.NavigateToCreateSet)
    }

    fun onStatsClick() = viewModelScope.launch {
        _eventChannel.send(MainUiEvent.NavigateToStats)
    }

    fun onNotificationsClick() = viewModelScope.launch {
        _eventChannel.send(MainUiEvent.NavigateToNotifications)
    }
}

sealed class MainUiEvent {
    object NavigateToReview : MainUiEvent()
    object NavigateToCreateSet : MainUiEvent()
    object NavigateToStats : MainUiEvent()
    object NavigateToNotifications : MainUiEvent()
}
