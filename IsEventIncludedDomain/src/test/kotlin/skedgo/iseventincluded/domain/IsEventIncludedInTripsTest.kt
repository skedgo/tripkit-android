package skedgo.iseventincluded.domain

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.just
import rx.observers.TestSubscriber

class IsEventIncludedInTripsTest {
  val isEventIncludedRepository: IsEventIncludedRepository = mock()
  val isEventIncludedInTrips: IsEventIncludedInTrips by lazy {
    IsEventIncludedInTrips(isEventIncludedRepository)
  }

  @Test fun eventShouldBeIncludedIfItIsNotSpecifiedYet() {
    whenever(isEventIncludedRepository.getIsEventIncluded(eq("Live long and prosper")))
        .thenReturn(just(IsEventIncluded.NOT_SPECIFIED_YET))

    val subscriber = TestSubscriber<Boolean>()
    isEventIncludedInTrips.execute("Live long and prosper")
        .subscribe(subscriber)

    subscriber.assertValue(true)
    subscriber.assertCompleted()
  }

  @Test fun eventShouldBeIncluded() {
    whenever(isEventIncludedRepository.getIsEventIncluded(eq("Live long and prosper")))
        .thenReturn(just(IsEventIncluded.YES))

    val subscriber = TestSubscriber<Boolean>()
    isEventIncludedInTrips.execute("Live long and prosper")
        .subscribe(subscriber)

    subscriber.assertValue(true)
    subscriber.assertCompleted()
  }

  @Test fun eventShouldNotBeIncluded() {
    whenever(isEventIncludedRepository.getIsEventIncluded(eq("Live long and prosper")))
        .thenReturn(just(IsEventIncluded.NO))

    val subscriber = TestSubscriber<Boolean>()
    isEventIncludedInTrips.execute("Live long and prosper")
        .subscribe(subscriber)

    subscriber.assertValue(false)
    subscriber.assertCompleted()
  }
}
