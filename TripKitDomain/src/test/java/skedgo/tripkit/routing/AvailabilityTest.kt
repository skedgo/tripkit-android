package skedgo.tripkit.routing

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class AvailabilityTest {
  val availableValue = "AVAILABLE"
  val missedPrebookingWindowValue = "MISSED_PREBOOKING_WINDOW"
  val cancelled = "CANCELLED"

  @Test fun valuesShouldBeCorrect() {
    assertThat(Availability.Available.value).isEqualTo(availableValue)
    assertThat(Availability.MissedPrebookingWindow.value).isEqualTo(missedPrebookingWindowValue)
    assertThat(Availability.Cancelled.value).isEqualTo(cancelled)
  }

  @Test fun shouldMapToAvailabilityFromString() {
    assertThat(availableValue.toAvailability()).isEqualTo(Availability.Available)
    assertThat(missedPrebookingWindowValue.toAvailability()).isEqualTo(Availability.MissedPrebookingWindow)
    assertThat(cancelled.toAvailability()).isEqualTo(Availability.Cancelled)
  }

  @Test fun shouldGiveNullIfStringIsNull() {
    val s: String? = null
    assertThat(s.toAvailability()).isNull()
  }

  @Test(expected = NoSuchElementException::class)
  fun shouldThrowErrorIfStringIsMalformed() {
    "Xcode sucks!".toAvailability()
  }
}
