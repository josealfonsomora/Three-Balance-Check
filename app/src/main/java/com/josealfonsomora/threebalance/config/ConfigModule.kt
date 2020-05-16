package com.josealfonsomora.threebalance.config

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun provideConfigModule() = module {
    viewModel {
        ConfigViewModel()
    }
}
