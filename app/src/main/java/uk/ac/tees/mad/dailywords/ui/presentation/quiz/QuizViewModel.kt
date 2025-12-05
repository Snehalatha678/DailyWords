package uk.ac.tees.mad.dailywords.ui.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuizRepository
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult

class QuizViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(QuizState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadQuizData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = QuizState()
        )

    private fun loadQuizData() {
        viewModelScope.launch {
            quizRepository.generateQuiz().collect { result ->
                when (result) {
                    is HttpResult.Loading -> _state.update { it.copy(isLoading = true) }
                    is HttpResult.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                questions = result.data
                            )
                        }
                    }
                    is HttpResult.Failure -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                // Handle error state, maybe show a message
                            )
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: QuizAction, onNavigate: () -> Unit = {}) {
        when (action) {
            is QuizAction.OnOptionSelected -> {
                if (!_state.value.isAnswerSubmitted) {
                    _state.update { it.copy(selectedAnswerIndex = action.index) }
                }
            }
            QuizAction.OnSubmitAnswer -> {
                val currentState = _state.value
                val currentQuestion = currentState.questions[currentState.currentQuestionIndex]
                val isCorrect = currentState.selectedAnswerIndex == currentQuestion.correctAnswerIndex

                _state.update {
                    it.copy(
                        isAnswerSubmitted = true,
                        score = if (isCorrect) it.score + 10 else it.score
                    )
                }
                
                if (currentState.currentQuestionIndex == currentState.questions.lastIndex) {
                    // Quiz finished, save result
                    viewModelScope.launch {
                        quizRepository.saveQuizResult(_state.value.score, currentState.questions.size)
                    }
                }
            }
            QuizAction.OnNextQuestion -> {
                val currentState = _state.value
                if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                    _state.update {
                        it.copy(
                            currentQuestionIndex = it.currentQuestionIndex + 1,
                            selectedAnswerIndex = null,
                            isAnswerSubmitted = false
                        )
                    }
                } else {
                    _state.update { it.copy(isQuizComplete = true) }
                }
            }
            QuizAction.OnRetryQuiz -> {
                _state.update {
                     QuizState()
                }
                loadQuizData()
            }
            QuizAction.OnReviewMistakes -> {
                _state.update {
                    it.copy(
                        isQuizComplete = false,
                        isReviewMode = true,
                        currentQuestionIndex = 0,
                        isAnswerSubmitted = true 
                    )
                }
            }
            QuizAction.OnFinishReview -> {
                // Navigate away
            }
            QuizAction.OnNavigateToHome -> onNavigate()
            QuizAction.OnNavigateToPractice -> onNavigate()
            QuizAction.OnNavigateToProfile -> onNavigate()
        }
    }
}
