package skedgo.tripkit.validbookingcount.domain

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.empty
import rx.Observable.just
import rx.observers.TestSubscriber
import skedgo.tripkit.account.domain.HasUserToken

class GetRemoteValidBookingCountTest {
  val validBookingCountRepository: ValidBookingCountRepository = mock()
  val hasUserToken: HasUserToken = mock()
  val getRemoteValidBookingCount: GetRemoteValidBookingCount by lazy {
    GetRemoteValidBookingCount(hasUserToken, validBookingCountRepository)
  }

  @Test fun shouldNotGetRemoteDataIfThereIsNoUserToken() {
    whenever(hasUserToken.execute()).thenReturn(just(false));
    whenever(validBookingCountRepository.getRemoteValidBookingCount())
        .thenReturn(empty())

    getRemoteValidBookingCount.execute().subscribe()

    verifyZeroInteractions(validBookingCountRepository)
  }

  @Test fun shouldGetRemoteDataIfThereIsUserToken() {
    whenever(hasUserToken.execute()).thenReturn(just(true));
    val expectedValidBookingCount = 1
    whenever(validBookingCountRepository.getRemoteValidBookingCount())
        .thenReturn(just(expectedValidBookingCount))

    val subscriber = TestSubscriber<Int>()
    getRemoteValidBookingCount.execute().subscribe(subscriber)

    subscriber.assertValue(expectedValidBookingCount)
    subscriber.assertCompleted()
    verify(validBookingCountRepository).getRemoteValidBookingCount()
  }
}
