package com.skedgo.tripkit.routing

import com.skedgo.tripkit.common.model.Location
import org.assertj.core.api.Java6Assertions.assertThat
import org.joda.time.DateTimeZone
import org.junit.Test

class LocationExtensionsKtTest {
  @Test fun shouldConvertToDateTimeZoneCorrectly() {
    val location = Location()
    location.timeZone = "Asia/Bangkok"
    assertThat(location.dateTimeZone)
        .isEqualTo(DateTimeZone.forID("Asia/Bangkok"))
  }
}