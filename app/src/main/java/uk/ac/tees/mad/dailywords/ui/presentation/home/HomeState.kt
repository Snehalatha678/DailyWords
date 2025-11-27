package uk.ac.tees.mad.dailywords.ui.presentation.home

data class HomeState(
    val word: String = "Word",
    val phonetic: String = "/fəˈnet.ɪk/",
    val partOfSpeech: String = "noun",
    val definitions: List<String> = listOf("The definition of the word will appear here."),
    val examples: List<String> = listOf("An example of how to use the word in a sentence."),
    val etymology: String? = null,
    val isBookmarked: Boolean = false,
    val isLoading: Boolean = false,
    val showEtymology: Boolean = false,
    val error: String? = null
)
