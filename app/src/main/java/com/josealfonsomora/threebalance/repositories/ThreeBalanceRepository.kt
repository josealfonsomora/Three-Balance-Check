package com.josealfonsomora.threebalance.repositories

import androidx.room.EmptyResultSetException
import com.josealfonsomora.entity.ThreeBalance
import com.josealfonsomora.threebalance.persistence.room.BalanceBucketDataModel
import com.josealfonsomora.threebalance.persistence.room.BalanceDao
import com.josealfonsomora.threebalance.persistence.room.BalanceDataModel
import com.josealfonsomora.threebalance.services.ThreeService
import io.reactivex.Single
import java.time.LocalDateTime
import java.time.ZoneOffset

class ThreeBalanceRepository(
    private val threeService: ThreeService,
    private val balanceDao: BalanceDao
) {
    fun getBalance(): Single<ThreeBalance> =
        Single.fromCallable {
            balanceDao.loadBalance() as ThreeBalance
        }.onErrorResumeNext { error ->
            if (error is EmptyResultSetException) {
                threeService
                    .login("julianreyeshdez@gmail.com", "p8574U")
                    .flatMap { threeService.getUser() }
                    .map {
                        Triple(
                            it.salesChannel,
                            it.customer.first().id,
                            it.customer.first().product.first().subscriptionId
                        )
                    }
                    .flatMap { (channel, customerId, subscriptionId) ->
                        threeService.getBalance(channel, subscriptionId, customerId)
                    }
                    .map {
                        BalanceDataModel(
                            balanceId = 1,
                            balances = it.balances as List<BalanceBucketDataModel>,
                            totalBalance = it.totalBalance,
                            cachedTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                        )
                    }.flatMap {
                        balanceDao.insertBalance(it)
                        Single.just(it)
                    }
            } else {
                throw error
            }
        }
}
