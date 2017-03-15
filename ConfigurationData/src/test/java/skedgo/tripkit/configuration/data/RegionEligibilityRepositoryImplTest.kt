package skedgo.tripkit.configuration.data

import android.content.Context
import android.content.SharedPreferences
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.observers.TestSubscriber
import skedgo.tripkit.configuration.data.RegionEligibilityRepositoryImpl.Companion.KEY_REGION_ELIGIBILITY
import skedgo.tripkit.configuration.domain.RegionEligibility

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class RegionEligibilityRepositoryImplTest {
  val prefs: SharedPreferences = RuntimeEnvironment.application.getSharedPreferences("", Context.MODE_PRIVATE)
  internal val repository: RegionEligibilityRepositoryImpl by lazy {
    RegionEligibilityRepositoryImpl(prefs)
  }

  @Test fun shouldEmitNothingIfThereIsNoRegionEligibility() {
    val subscriber = TestSubscriber<RegionEligibility>()
    repository.getRegionEligibility()
        .subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoValues()
  }

  @Test fun shouldEmitRegionEligibility() {
    prefs.edit().putString(KEY_REGION_ELIGIBILITY, "beta").apply()

    val subscriber = TestSubscriber<RegionEligibility>()
    repository.getRegionEligibility()
        .subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertValue(RegionEligibility("beta"))
  }

  @Test fun shouldSetRegionEligibilityValue() {
    val expectedRegionEligibility = RegionEligibility("beta")
    repository.setRegionEligibility(expectedRegionEligibility)
        .subscribe()

    val value = prefs.getString(KEY_REGION_ELIGIBILITY, null)
    assertThat(value).isEqualTo(expectedRegionEligibility.value)
  }
}
