package skedgo.iseventincluded.domain

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.empty
import rx.observers.TestSubscriber

class SetIsEventIncludedInTripsTest {
  val isEventIncludedRepository: IsEventIncludedRepository = mock()
  val setIsEventIncludedInTrips: SetIsEventIncludedInTrips by lazy {
    SetIsEventIncludedInTrips(isEventIncludedRepository)
  }

  @Test fun shouldIncludeEvent() {
    whenever(isEventIncludedRepository.setIsEventIncluded(any(), any()))
        .thenReturn(empty())

    val subscriber = TestSubscriber<Unit>()
    setIsEventIncludedInTrips.execute("spock@vulcan.com", true)
        .subscribe(subscriber)

    subscriber.assertCompleted()
    verify(isEventIncludedRepository).setIsEventIncluded("spock@vulcan.com", IsEventIncluded.YES);
  }

  @Test fun shouldNotIncludeEvent() {
    whenever(isEventIncludedRepository.setIsEventIncluded(any(), any()))
        .thenReturn(empty())

    val subscriber = TestSubscriber<Unit>()
    setIsEventIncludedInTrips.execute("spock@vulcan.com", false)
        .subscribe(subscriber)

    subscriber.assertCompleted()
    verify(isEventIncludedRepository).setIsEventIncluded("spock@vulcan.com", IsEventIncluded.NO);
  }
}
