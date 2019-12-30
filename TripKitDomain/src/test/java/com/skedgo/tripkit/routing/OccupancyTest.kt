package com.skedgo.tripkit.routing

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class OccupancyTest {
  @Test fun shouldGiveNullIfStringIsNull() {
    val s: String? = null
    assertThat(s.toOccupancy()).isNull()
  }

  @Test fun valuesShouldBeCorrect() {
    assertThat(Occupancy.Empty.value).isEqualTo("EMPTY")
    assertThat(Occupancy.ManySeatsAvailable.value).isEqualTo("MANY_SEATS_AVAILABLE")
    assertThat(Occupancy.FewSeatsAvailable.value).isEqualTo("FEW_SEATS_AVAILABLE")
    assertThat(Occupancy.StandingRoomOnly.value).isEqualTo("STANDING_ROOM_ONLY")
    assertThat(Occupancy.CrushedStandingRoomOnly.value).isEqualTo("CRUSHED_STANDING_ROOM_ONLY")
    assertThat(Occupancy.Full.value).isEqualTo("FULL")
    assertThat(Occupancy.NotAcceptingPassengers.value).isEqualTo("NOT_ACCEPTING_PASSENGERS")
  }

  @Test(expected = NoSuchElementException::class)
  fun shouldThrowErrorIfStringIsMalformed() {
    "Xcode sucks!".toOccupancy()
  }
}
