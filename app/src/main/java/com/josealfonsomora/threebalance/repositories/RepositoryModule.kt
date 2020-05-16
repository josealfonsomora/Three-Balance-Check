package com.josealfonsomora.threebalance.repositories

import org.koin.dsl.module

fun provideRepositoryModule() = module {
    single { CookiesRepository(get(), get()) }
}
