package skedgo.tripkit.validbookingcount.data

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ValidBookingCountRepositoryImplTest {
  private val api: ValidBookingCountApi = mock()
  private val prefs: SharedPreferences by lazy {
    RuntimeEnvironment.application.getSharedPreferences("", Context.MODE_PRIVATE)
  }
  private val repository: ValidBookingCountRepositoryImpl by lazy {
    ValidBookingCountRepositoryImpl(api, prefs)
  }

  @Test fun shouldSaveToDiskAfterFetchingFromRemote() {
    whenever(api.fetchValidBookingCount())
        .then {
          val response: ValidBookingCountResponse = mock()
          whenever(response.count()).thenReturn(2)
          Observable.just(response)
        }

    val subscriber = TestSubscriber<Int>()
    repository.getRemoteValidBookingCount().subscribe(subscriber)

    subscriber.assertValue(2)
    subscriber.assertCompleted()
    assertThat(prefs.getInt("validBookingCount", 0)).isEqualTo(2)
  }

  @Test fun shouldReEmitLocalValidBookingCount() {
    val subscriber = TestSubscriber<Int>()
    repository.getLocalValidBookingCount().subscribe(subscriber)

    subscriber.assertNoValues()

    prefs.edit().putInt("validBookingCount", 1).apply()
    subscriber.assertValue(1)

    prefs.edit().putInt("validBookingCount", 2).apply()
    subscriber.assertValues(1, 2)
  }
}
