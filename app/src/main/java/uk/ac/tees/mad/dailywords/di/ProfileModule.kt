package uk.ac.tees.mad.dailywords.di

import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.presentation.profile.ProfileViewModel

val profileModule = module {
    viewModel {
        ProfileViewModel(
            auth = get(),
            firestore = get(),
            storageRepository = get(),
            notificationScheduler = get(),
            ioDispatcher = Dispatchers.IO
        )
    }
}