package skedgo.tripkit.android

import com.google.android.gms.gcm.GcmNetworkManager
import com.nhaarman.mockito_kotlin.mock
import com.skedgo.android.tripkit.RegionService
import com.skedgo.android.tripkit.TripKitAndroidRobolectricTest
import org.amshove.kluent.shouldBe
import org.junit.Test
import rx.Observable
import rx.Observable.empty

@Suppress("IllegalIdentifier")
class FetchRegionsServiceTest : TripKitAndroidRobolectricTest() {
  val regionService = mock<RegionService> {
    on { refreshAsync() }.thenReturn(empty())
  }
  val tripKit = mock<TripKit> {
    on { regionService }.then { regionService }
  }

  @Test fun `should reschedule if emitting nothing`() {
    Observable.just(tripKit)
        .refreshRegions()
        .shouldBe(GcmNetworkManager.RESULT_RESCHEDULE)
  }
}
