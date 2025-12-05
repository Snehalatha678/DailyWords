package uk.ac.tees.mad.dailywords.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import org.koin.dsl.module

val supabaseModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = "https://qybsmcrlxlzayxwkpdvv.supabase.co",
            supabaseKey = "sb_publishable_UNueQMl98wGPuWlaYdlkbw_LMUgnd_G"
        ) {
            install(Storage)
        }
    }
    single<Storage> {
        get<SupabaseClient>().storage
    }
}
