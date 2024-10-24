package com.skedgo.tripkit.validbookingcount.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable.empty
import org.junit.Test

//import com.skedgo.tripkit.account.domain.HasUserToken

class GetRemoteValidBookingCountTest {
    val validBookingCountRepository: ValidBookingCountRepository = mock()

    //val hasUserToken: HasUserToken = mock()
    val getRemoteValidBookingCount: GetRemoteValidBookingCount by lazy {
        GetRemoteValidBookingCount(/*hasUserToken, */validBookingCountRepository)
    }

    // TODO: Unit test - refactor
    /*
    @Test
    fun shouldNotGetRemoteDataIfThereIsNoUserToken() {
        //whenever(hasUserToken.execute()).thenReturn(just(false));
        whenever(validBookingCountRepository.getRemoteValidBookingCount())
            .thenReturn(empty())

        getRemoteValidBookingCount.execute().subscribe()

        verifyZeroInteractions(validBookingCountRepository)
    }
     */

    /*
    @Test fun shouldGetRemoteDataIfThereIsUserToken() {
      //whenever(hasUserToken.execute()).thenReturn(just(true));
      val expectedValidBookingCount = 1
      whenever(validBookingCountRepository.getRemoteValidBookingCount())
          .thenReturn(just(expectedValidBookingCount))

      val subscriber = getRemoteValidBookingCount.execute().test()

      subscriber.assertValue(expectedValidBookingCount)
      subscriber.assertComplete()
      verify(validBookingCountRepository).getRemoteValidBookingCount()
    }
    */
}
