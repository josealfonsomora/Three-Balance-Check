package com.josealfonsomora.threebalance.services

import org.koin.dsl.module

fun provideServiceModule() = module {
    single {
        ThreeService(get())
    }

    single {
        LoginService(
            get(),
            get()
        )
    }
}
