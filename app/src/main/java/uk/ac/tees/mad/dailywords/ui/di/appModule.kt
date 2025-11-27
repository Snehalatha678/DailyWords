package uk.ac.tees.mad.dailywords.ui.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.data.AuthRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.util.GsonParser
import uk.ac.tees.mad.dailywords.ui.data.word.EtymologyRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.word.WordRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.word.local.Converters
import uk.ac.tees.mad.dailywords.ui.data.word.local.WordDatabase
import uk.ac.tees.mad.dailywords.ui.data.word.remote.WordApi
import uk.ac.tees.mad.dailywords.ui.domain.AuthRepository
import uk.ac.tees.mad.dailywords.ui.domain.util.JsonParser
import uk.ac.tees.mad.dailywords.ui.domain.word.EtymologyRepository
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetEtymology
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetRandomWord
import uk.ac.tees.mad.dailywords.ui.presentation.auth.create_account.CreateAccountViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.auth.forgot.ForgotViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.auth.login.LoginViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.home.HomeViewModel

val appModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            defaultRequest {
                url(WordApi.BASE_URL)
            }
        }
    }

    single {
        WordApi(get())
    }

    single<WordRepository> {
        WordRepositoryImpl(get(), get())
    }

    single<EtymologyRepository> {
        EtymologyRepositoryImpl(androidApplication())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }

    single {
        FirebaseAuth.getInstance()
    }

    single {
        FirebaseFirestore.getInstance()
    }

    single<WordDatabase> {
        androidx.room.Room.databaseBuilder(
            androidApplication(),
            WordDatabase::class.java,
            WordDatabase.DATABASE_NAME
        ).addTypeConverter(Converters(get()))
            .build()
    }

    single {
        get<WordDatabase>().wordDao
    }

    single<JsonParser> {
        GsonParser(Gson())
    }

    single {
        GetRandomWord(get())
    }

    single {
        GetEtymology(get())
    }

    viewModel {
        HomeViewModel(get(), get())
    }

    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        CreateAccountViewModel(get())
    }

    viewModel {
        ForgotViewModel(get())
    }
}