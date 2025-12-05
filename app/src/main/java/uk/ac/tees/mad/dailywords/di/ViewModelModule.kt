package uk.ac.tees.mad.dailywords.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.presentation.auth.create_account.CreateAccountViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.auth.forgot.ForgotViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.auth.login.LoginViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.home.HomeViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.practice.PracticeViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.quiz.QuizViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.splash.SplashViewModel

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), androidApplication()) }
    viewModel { LoginViewModel(get()) }
    viewModel { CreateAccountViewModel(get()) }
    viewModel { ForgotViewModel(get()) }
    viewModel { params -> PracticeViewModel(get(), params.get(), androidApplication()) }
    viewModel { SplashViewModel(get()) }
    viewModel { QuizViewModel(get()) }
}