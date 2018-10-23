package skedgo.tripkit.android

import com.nhaarman.mockitokotlin2.mock
import com.skedgo.android.tripkit.RegionService
import com.skedgo.android.tripkit.TripKitAndroidRobolectricTest
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.junit.Test
import rx.Completable
import rx.Observable

@Suppress("IllegalIdentifier")
class FetchRegionsServiceTest : TripKitAndroidRobolectricTest() {
  val regionService = mock<RegionService> {
    on { refreshAsync() }.thenReturn(Completable.complete())
  }
  val tripKit = mock<TripKit> {
    on { regionService }.then { regionService }
  }

  @Test fun `should succeed if emitting nothing`() {
    Observable.just(tripKit)
        .refreshRegions()
        .test()
        .assertCompleted()
  }

  @Test fun `should reschedule if encountering error`() {
    When calling regionService.refreshAsync() itReturns Completable.error { RuntimeException() }
    Observable.just(tripKit)
        .refreshRegions()
        .test()
        .assertError(RuntimeException::class.java)
  }
}
