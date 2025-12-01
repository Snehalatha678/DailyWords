package uk.ac.tees.mad.dailywords.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
import okhttp3.Dns
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import uk.ac.tees.mad.dailywords.ui.data.AuthRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.etymology.EtymologyRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.etymology.remote.EtymologyApi
import uk.ac.tees.mad.dailywords.ui.data.word.WordRepositoryImpl
import uk.ac.tees.mad.dailywords.ui.data.word.local.WordDatabase
import uk.ac.tees.mad.dailywords.ui.data.word.remote.WordApi
import uk.ac.tees.mad.dailywords.ui.domain.AuthRepository
import uk.ac.tees.mad.dailywords.ui.domain.etymology.EtymologyRepository
import uk.ac.tees.mad.dailywords.ui.domain.util.NetworkManager
import uk.ac.tees.mad.dailywords.ui.domain.util.TextToSpeechProvider
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.AddBookmark
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetBookmarkedWords
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetRandomWord
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.RemoveBookmark

val appModule = module {

    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Ktor
    single {
        HttpClient(OkHttp) {
            engine {
                config {
                    dns(Dns.SYSTEM)
                }
            }
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
        androidx.room.Room.databaseBuilder(
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
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    //UseCases
    factory { GetRandomWord(get()) }
    factory { AddBookmark(get()) }
    factory { RemoveBookmark(get()) }
    factory { GetBookmarkedWords(get()) }

    // TextToSpeech
    single { TextToSpeechProvider(androidApplication(), null) }

    // NetworkManager
    single {
        NetworkManager(androidApplication())
    }
}