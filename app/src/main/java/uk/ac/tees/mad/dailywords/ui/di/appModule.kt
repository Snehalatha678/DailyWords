package uk.ac.tees.mad.dailywords.ui.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.data.AuthRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.domain.AuthRepository
import uk.ac.tees.mad.dailywords.ui.presentation.auth.create_account.CreateAccountViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.auth.forgot.ForgotViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.auth.login.LoginViewModel

val appModule = module {

    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // ViewModels
    viewModel { CreateAccountViewModel(get()) }
    viewModel { ForgotViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}
