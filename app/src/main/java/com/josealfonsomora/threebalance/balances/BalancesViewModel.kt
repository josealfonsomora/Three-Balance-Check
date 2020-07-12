package com.josealfonsomora.threebalance.balances

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.josealfonsomora.threebalance.services.ThreeService

class BalancesViewModel(threeService: ThreeService) : ViewModel() {

    val totalBalance = ObservableField<CharSequence>()

}
