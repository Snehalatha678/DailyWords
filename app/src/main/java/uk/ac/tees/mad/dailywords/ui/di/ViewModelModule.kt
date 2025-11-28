package uk.ac.tees.mad.dailywords.ui.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.presentation.auth.login.LoginViewModel

val viewModelModule = module {

    viewModel {
        LoginViewModel(get())
    }
}