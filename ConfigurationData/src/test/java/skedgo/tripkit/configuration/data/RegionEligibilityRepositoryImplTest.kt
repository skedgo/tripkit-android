package skedgo.tripkit.configuration.data

import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.Observable
import rx.observers.TestSubscriber
import skedgo.tripkit.configuration.data.RegionEligibilityRepositoryImpl.Companion.KEY_REGION_ELIGIBILITY
import skedgo.tripkit.configuration.domain.RegionEligibility

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class RegionEligibilityRepositoryImplTest {
  val prefs = RuntimeEnvironment.application.getSharedPreferences("", Context.MODE_PRIVATE)
  internal val repository: RegionEligibilityRepositoryImpl by lazy {
    RegionEligibilityRepositoryImpl(prefs)
  }

  @Test fun shouldHaveNoValue() {
    val subscriber = TestSubscriber<RegionEligibility>()
    repository.getRegionEligibility()
        .subscribe(subscriber)
    subscriber.awaitTerminalEvent()

    subscriber.assertNoValues()
  }

  @Test fun shouldHaveRegionEligibilityValue() {
    prefs.edit().putString(KEY_REGION_ELIGIBILITY, "region").apply()

    val subscriber = TestSubscriber<RegionEligibility>()
    repository.getRegionEligibility()
        .subscribe(subscriber)
    subscriber.awaitTerminalEvent()

    subscriber.assertValue(RegionEligibility("region"))
  }

  @Test fun shouldSetRegionEligibilityValue() {
    val regionEligibilityExpected = RegionEligibility("region")
    repository.setRegionEligibility(regionEligibilityExpected)
        .subscribe()

    val subscriber = TestSubscriber<RegionEligibility>()
    Observable.fromCallable { prefs.getString(KEY_REGION_ELIGIBILITY, null) }
        .map(::RegionEligibility)
        .subscribe(subscriber)

    subscriber.assertValue(regionEligibilityExpected)
  }
}
