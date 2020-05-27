package com.josealfonsomora.threebalance.login

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun provideLoginModule() = module {
    viewModel { LoginViewModel(get(), get()) }
}
