package com.skedgo.tripkit.validbookingcount.data

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import io.reactivex.Observable

@RunWith(AndroidJUnit4::class)
class ValidBookingCountRepositoryImplTest {
    private val api: ValidBookingCountApi = mock()
    private val prefs: SharedPreferences by lazy {
        ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("", Context.MODE_PRIVATE)
    }
    private val repository: ValidBookingCountRepositoryImpl by lazy {
        ValidBookingCountRepositoryImpl(api, prefs)
    }

    @Test
    fun shouldSaveToDiskAfterFetchingFromRemote() {
        whenever(api.fetchValidBookingCount())
            .then {
                val response: ValidBookingCountResponse = mock()
                whenever(response.count()).thenReturn(2)
                Observable.just(response)
            }


        val subscriber = repository.getRemoteValidBookingCount().test()

        subscriber.assertValue(2)
        subscriber.assertComplete()
        assertThat(prefs.getInt("validBookingCount", 0)).isEqualTo(2)
    }

    @Test
    fun shouldReEmitLocalValidBookingCount() {
        val subscriber = repository.getLocalValidBookingCount().test()

        subscriber.assertNoValues()

        prefs.edit().putInt("validBookingCount", 1).apply()
        subscriber.assertValue(1)

        prefs.edit().putInt("validBookingCount", 2).apply()
        subscriber.assertValues(1, 2)
    }
}
