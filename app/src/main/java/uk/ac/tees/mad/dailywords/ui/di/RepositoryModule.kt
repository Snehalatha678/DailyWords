package uk.ac.tees.mad.dailywords.ui.di

import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.data.quiz.QuizRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.word.WordRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuizRepository
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

val repositoryModule = module {
    single<WordRepository> { WordRepositoryImpl(get(), get(), get()) }
    single<QuizRepository> { QuizRepositoryImpl(get()) }
}
