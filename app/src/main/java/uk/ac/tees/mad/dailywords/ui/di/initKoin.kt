package uk.ac.tees.mad.dailywords.ui.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import uk.ac.tees.mad.dailywords.di.appModule

fun initKoin(config : KoinAppDeclaration?=null){

    startKoin {
        config?.invoke(this)
        modules(

            appModule,
        )

    }
}