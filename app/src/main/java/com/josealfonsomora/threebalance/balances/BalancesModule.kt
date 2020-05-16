package com.josealfonsomora.threebalance.balances

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun provideBalancesModule() = module {
    viewModel {
        BalancesViewModel(get())
    }
}
