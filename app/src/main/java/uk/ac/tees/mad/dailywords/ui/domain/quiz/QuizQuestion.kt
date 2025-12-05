package uk.ac.tees.mad.dailywords.ui.domain.quiz

data class QuizQuestion(
    val id: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val type: QuestionType,
    val explanation: String
)

enum class QuestionType {
    MEANING, SPELLING, USAGE
}
