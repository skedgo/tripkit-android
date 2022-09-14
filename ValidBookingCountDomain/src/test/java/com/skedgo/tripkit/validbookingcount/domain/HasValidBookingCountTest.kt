package com.skedgo.tripkit.validbookingcount.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import io.reactivex.Observable

class HasValidBookingCountTest {
  val validBookingCountRepository: ValidBookingCountRepository = mock()
  val hasValidBookingCount: HasValidBookingCount by lazy {
    HasValidBookingCount(validBookingCountRepository)
  }

  @Test fun shouldBeTrueIfHavingMoreThanZero() {
    whenever(validBookingCountRepository.getLocalValidBookingCount())
        .thenReturn(Observable.just(1))

    val subscriber = hasValidBookingCount.execute().test()

    subscriber.assertValue(true)
    subscriber.assertComplete()
  }

  @Test fun shouldBeFalseIfHavingLessThanOne() {
    whenever(validBookingCountRepository.getLocalValidBookingCount())
        .thenReturn(Observable.just(0))

    val subscriber = hasValidBookingCount.execute().test()

    subscriber.assertValue(false)
    subscriber.assertComplete()
  }
}
