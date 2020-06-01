package com.josealfonsomora.threebalance.workers

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.josealfonsomora.threebalance.factories.NOTIFICATION_ID
import com.josealfonsomora.threebalance.factories.NotificationFactory
import com.josealfonsomora.threebalance.services.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.Response
import java.math.BigDecimal

class CheckThreeBalanceWorkerTest : KoinTest {

    private val email = "user@email.com"
    private val password = "password"
    private val priorResponse: okhttp3.Response = mock {
        on { code() }.thenReturn(302)
    }
    private val rawResponse = mock<okhttp3.Response> {
        on { priorResponse() }.thenReturn(priorResponse)
        on { code() }.thenReturn(302)
    }
    private val response302: Response<ResponseBody> = mock {
        on { code() }.thenReturn(200)
        on { isSuccessful }.thenReturn(true)
        on { raw() }.thenReturn(rawResponse)
    }
    private val notification: Notification = mock()
    private val notificationFactory: NotificationFactory = mock {
        on { newNotification(any(), any(), any(), any(), any()) }.thenReturn(notification)
    }
    private val context: Context = mock()
    private val loginService: LoginService = mock {
        on { login(email, password) }.thenReturn(Single.just(response302))
    }
    private val editor: SharedPreferences.Editor = mock {
        on { putString(any(), any()) }.thenReturn(it)
    }
    private val sharedPreferences: SharedPreferences = mock {
        on { edit() }.thenReturn(editor)
        on { getString("email", "") }.thenReturn(email)
        on { getString("password", "") }.thenReturn(password)

    }

    private val subscriptionId = "subscriptionId"
    private val threeCustomerProduct: Product = mock {
        on { subscriptionId }.thenReturn(subscriptionId)
    }

    private val customerId: Long = 1234
    private val threeCustomer: Customer = mock {
        on { id }.thenReturn(customerId)
        on { product }.thenReturn(listOf(threeCustomerProduct))
    }
    private val salesChannel = "salesChannel"
    private val threeResponseMock: ThreeResponse = mock {
        on { salesChannel }.thenReturn(salesChannel)
        on { customer }.thenReturn(listOf(threeCustomer))
    }

    private val totalBalance = BigDecimal(1234)
    private val threeBalanceBucket: Bucket = mock()
    private val threeBalanceResponse = ThreeBalanceResponse(
        buckets = listOf(threeBalanceBucket),
        totalBalance = totalBalance
    )

    private val allowanceAccumulator: AllowanceAccumulator = mock()
    private val threeAllowanceResponse: ThreeAllowanceResponse = mock {
        on { accumulators }.thenReturn(listOf(allowanceAccumulator))
    }
    private val threeService: ThreeService = mock {
        on { getUser() }.thenReturn(Single.just(threeResponseMock))
        on { getBalance(salesChannel, subscriptionId, customerId) }.thenReturn(
            Single.just(
                threeBalanceResponse
            )
        )
        on {
            getAllowance(
                salesChannel,
                customerId
            )
        }.thenReturn(Single.just(threeAllowanceResponse))
    }

    private val notificationManager: NotificationManager = mock()

    @Test
    fun retrievesDataFromThreeToDisplayItAsNotification() {
        startKoin {
            modules(module {
                single { notificationFactory }
                single { sharedPreferences }
                single { loginService }
                single { threeService }
                single { notificationManager }
            })
        }
        val workerParameters: WorkerParameters = mock()

        val worker = CheckThreeBalanceWorker(context, workerParameters)

        val observer = worker.createWork().test()

        verify(notificationManager).cancelAll()
        verify(notificationManager).notify(
            NOTIFICATION_ID,
            notification
        )
        observer.assertValue(ListenableWorker.Result.success())
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }
}
