package uk.ac.tees.mad.dailywords.di

import android.content.Context
import android.net.ConnectivityManager
import android.speech.tts.TextToSpeech
import androidx.room.Room
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.data.etymology.EtymologyRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.etymology.remote.EtymologyApi
import uk.ac.tees.mad.dailywords.ui.data.word.WordRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.word.local.WordDatabase
import uk.ac.tees.mad.dailywords.ui.data.word.remote.WordApi
import uk.ac.tees.mad.dailywords.ui.domain.etymology.EtymologyRepository
import uk.ac.tees.mad.dailywords.ui.domain.util.NetworkManager
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.AddBookmark
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetBookmarkedWords
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetRandomWord
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.RemoveBookmark
import uk.ac.tees.mad.dailywords.ui.presentation.home.HomeViewModel

val appModule = module {

    // Ktor
    single {
        HttpClient(OkHttp) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json { 
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30000L
                connectTimeoutMillis = 30000L
                socketTimeoutMillis = 30000L
            }
        }
    }

    // WordApi
    single { WordApi(get()) }
    single { EtymologyApi(get()) }

    // Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            WordDatabase::class.java,
            WordDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    // Repository
    single<WordRepository> {
        WordRepositoryImpl(
            api = get(),
            dao = get<WordDatabase>().wordDao,
            networkManager = get()
        )
    }
    single<EtymologyRepository> { EtymologyRepositoryImpl(get()) }
    
    //UseCases
    factory { GetRandomWord(get(), get()) }
    factory { AddBookmark(get()) }
    factory { RemoveBookmark(get()) }
    factory { GetBookmarkedWords(get()) }

    // TextToSpeech
    single {
        TextToSpeech(androidApplication(), null)
    }

    // NetworkManager
    single {
        NetworkManager(androidApplication())
    }

    // ViewModel
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), androidApplication()) }
}