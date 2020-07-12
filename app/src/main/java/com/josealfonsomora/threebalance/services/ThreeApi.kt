package com.josealfonsomora.threebalance.services

import com.google.gson.annotations.SerializedName
import com.josealfonsomora.entity.ThreeBalance
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigDecimal

interface ThreeApi {
    @GET("/my3r/rp-server/authentication/v1/user")
    fun getUser(): Single<ThreeResponse>

    @GET("/my3r/rp-server/billingcare/customer/{customerId}/subscriptions-prepaid-buckets")
    fun getBalance(
        @Path("customerId") customerId: Long,
        @Query("salesChannel") selfService: String,
        @Query("subscriptionId") subscriptionId: String,
        @Query("filter") filter: String = "expired"
    ): Single<ThreeBalanceResponse>

    @GET("/my3r/rp-server/billingcare/customer/{customerId}/unbilled-usage-accumulators?salesChannel=selfService")
    fun getAllowance(
        @Path("customerId") customerId: Long,
        @Query("salesChannel") selfService: String
    ): Single<ThreeAllowanceResponse>
}

class ThreeAllowanceResponse(
    val cycleEndDate: String, //Sun May 17 00:00:00 IST 2020"
    val cycleStartDateL: String, //"Mon Feb 17 00:00:00 GMT 2020"
    val daysToNextBill: Int,
    val accumulators: List<AllowanceAccumulator>
)

data class AllowanceAccumulator(
    val id: Long,
    val currency: String,
    val amount: BigDecimal,
    val name: String,
    val pricePackageID: Long,
    val quota: BigDecimal,
    val remainingQuota: BigDecimal,
    val subscriptionId: Long,
    val type: String,
    val unitOfMeasurement: String,
    val allowanceType: String,
    val usageType: String,
    val utilizedQuotaPercentage: Float,
    val volume: Float,
    val description: String,
    val remainingQuotaPercentage: Float,
    val expirationDate: String //2222-01-01T19:00:00Z
)

data class ThreeResponse(
    val userId: String,
    val salesChannel: String,
    val isAnonymous: Boolean,
    val accessLevel: String,
    val currentCustomerId: Long,
    val isAffinity: Boolean,
    val individual: Individual,
    val customer: List<Customer>
)

class Individual(
    val id: Long,
    val firstName: String,
    val lastName: String
)

class Customer(
    val id: Long,
    val product: List<Product>,
    val financialAccount: List<FinancialAccount>,
    val individualRole: String,
    val paymentManagementType: String,
    val owningIndividual: Individual,
    val subType: String,
    val type: String,
    val selected: Boolean
)

class Product(
    val id: Long,
    val subscriptionId: String,
    val primaryResource: String,
    val status: String
)

class FinancialAccount(val id: Long)

data class ThreeBalanceResponse(
    @SerializedName("buckets")
    override val balances: List<ThreeBalance.Bucket>,
    override val totalBalance: BigDecimal
): ThreeBalance

data class BucketResponse(
    override val currency: String,
    override val id: Long,
    override val name: String,
    override val daysToExpire: Int,
    @SerializedName("balanceAmount")
    override val amount: BigDecimal,
    @SerializedName("balanceExpiryDate")
    override val expiryDate: Long,
    override val subscriptionId: String
): ThreeBalance.Bucket
