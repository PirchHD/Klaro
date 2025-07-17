package com.example.klaro.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klaro.domain.repository.interfaces.LearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.klaro.ui.state.LearningUiState

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val repository: LearningRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){


    private val setId: String = requireNotNull(savedStateHandle["setId"])

    private val _uiState = MutableStateFlow(LearningUiState())

    val uiState: StateFlow<LearningUiState> = _uiState.asStateFlow()

    init
    {
        loadFlashCardSet()
        loadAllData()
    }

    private fun loadFlashCardSet()
    {
        viewModelScope.launch {
            val set = repository.getFlashcardSetById(setId)
            _uiState.update { it.copy(flashcardSet = set) }
        }
    }

    private fun loadAllData()
    {
        viewModelScope.launch {
            val progress = repository.getReviewProgress(setId) ?: return@launch

            _uiState.update { it.copy(
                reviewProgress = progress,
                isLoading = false
            ) }
        }
    }


    fun onFlip() = _uiState.update { it.copy(isFlipped = !it.isFlipped) }


    fun onKnow() {
        val card = _uiState.value.flashcardSet?.cards?.get(_uiState.value.currentIndex) ?: return
        val reviewProgressId = _uiState.value.reviewProgress.id

        viewModelScope.launch {
            nextCard()
            val progress = repository.createOrSetReview(cardId = card.id, isKnow = true, reviewProgressId = reviewProgressId)
            _uiState.update { it.copy(reviewProgress = progress) }
        }
    }

    fun onDoNotKnow() {
        val card = _uiState.value.flashcardSet?.cards?.get(_uiState.value.currentIndex) ?: return
        val reviewProgressId = _uiState.value.reviewProgress.id

        viewModelScope.launch {
            nextCard()
            val progress = repository.createOrSetReview(cardId = card.id, isKnow = false, reviewProgressId = reviewProgressId)
            _uiState.update { it.copy(reviewProgress = progress) }
        }
    }

    private fun nextCard() {
        _uiState.update { state ->
            val lastIndex = state.flashcardSet?.cards?.lastIndex ?: 0
            val next = (state.currentIndex + 1).coerceAtMost(lastIndex)
            state.copy(currentIndex = next, isFlipped = false)
        }
    }
}
