// TODO: Unit test - refactor
/* Disabled class due to unresolved references
package com.skedgo.tripkit.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.skedgo.TripKit
import com.skedgo.tripkit.TripKitAndroidRobolectricTest
import com.skedgo.tripkit.data.regions.RegionService
import io.reactivex.Completable
import io.reactivex.Observable
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("IllegalIdentifier")
@RunWith(AndroidJUnit4::class)
class FetchRegionsServiceTest : TripKitAndroidRobolectricTest() {
    val regionService = mock<RegionService> {
        on { refreshAsync() }.thenReturn(Completable.complete())
    }
    val tripKit = mock<TripKit> {
        on { regionService }.then { regionService }
    }

    @Test
    fun `should succeed if emitting nothing`() {
        Observable.just(tripKit)
            .refreshRegions()
            .test()
            .assertComplete()
    }

    @Test
    fun `should reschedule if encountering error`() {
        When calling regionService.refreshAsync() itReturns Completable.error { RuntimeException() }
        Observable.just(tripKit)
            .refreshRegions()
            .test()
            .assertError(RuntimeException::class.java)
    }
}
 */
