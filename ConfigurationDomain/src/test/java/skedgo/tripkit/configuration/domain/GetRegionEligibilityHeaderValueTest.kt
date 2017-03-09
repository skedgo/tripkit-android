package skedgo.tripkit.configuration.domain

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.empty
import rx.Observable.just
import rx.observers.TestSubscriber

class GetRegionEligibilityHeaderValueTest {
  val regionEligibilityRepository: RegionEligibilityRepository = mock()
  val getRegionEligibilityHeaderValue: GetRegionEligibilityHeaderValue by lazy {
    GetRegionEligibilityHeaderValue(regionEligibilityRepository)
  }

  @Test fun shouldTakeDefaultRegionEligibility() {
    whenever(regionEligibilityRepository.getRegionEligibility())
        .thenReturn(empty())

    val defaultRegionEligibility = RegionEligibility("Live long, and prosper.")
    val subscriber = TestSubscriber<String>()
    getRegionEligibilityHeaderValue.execute(defaultRegionEligibility).subscribe(subscriber)

    subscriber.assertValue("Live long, and prosper.")
    subscriber.assertCompleted()
  }

  @Test fun shouldTakeRegionEligibilityFromRepository() {
    val regionEligibility = RegionEligibility("Fascinating!")
    whenever(regionEligibilityRepository.getRegionEligibility())
        .thenReturn(just(regionEligibility))

    val defaultRegionEligibility = RegionEligibility("Live long, and prosper.")
    val subscriber = TestSubscriber<String>()
    getRegionEligibilityHeaderValue.execute(defaultRegionEligibility).subscribe(subscriber)

    subscriber.assertValue("Fascinating!")
    subscriber.assertCompleted()
  }
}
