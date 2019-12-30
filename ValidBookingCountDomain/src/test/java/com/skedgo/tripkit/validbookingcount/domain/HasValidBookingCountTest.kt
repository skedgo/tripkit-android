package com.skedgo.tripkit.validbookingcount.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import io.reactivex.Observable
import io.reactivex.observers.TestSubscriber

class HasValidBookingCountTest {
  val validBookingCountRepository: ValidBookingCountRepository = mock()
  val hasValidBookingCount: HasValidBookingCount by lazy {
    HasValidBookingCount(validBookingCountRepository)
  }

  @Test fun shouldBeTrueIfHavingMoreThanZero() {
    whenever(validBookingCountRepository.getLocalValidBookingCount())
        .thenReturn(Observable.just(1))

    val subscriber = TestSubscriber<Boolean>()
    hasValidBookingCount.execute().subscribe(subscriber)

    subscriber.assertValue(true)
    subscriber.assertCompleted()
  }

  @Test fun shouldBeFalseIfHavingLessThanOne() {
    whenever(validBookingCountRepository.getLocalValidBookingCount())
        .thenReturn(Observable.just(0))

    val subscriber = TestSubscriber<Boolean>()
    hasValidBookingCount.execute().subscribe(subscriber)

    subscriber.assertValue(false)
    subscriber.assertCompleted()
  }
}
