package com.josealfonsomora.threebalance.balances

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.josealfonsomora.threebalance.databinding.ActivityBalancesBinding
import org.koin.android.viewmodel.ext.android.viewModel

class BalancesActivity : ComponentActivity() {
    lateinit var binding: ActivityBalancesBinding
    private val viewModel: BalancesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBalancesBinding.inflate(layoutInflater)

    }

    companion object {
        fun launch(activity: ComponentActivity) {
            activity.startActivity(Intent(activity, BalancesActivity::class.java))
        }
    }
}
