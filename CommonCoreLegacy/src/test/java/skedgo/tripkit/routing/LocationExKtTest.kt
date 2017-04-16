package skedgo.tripkit.routing

import com.skedgo.android.common.model.Location
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTimeZone
import org.junit.Test

class LocationExKtTest {
  @Test fun shouldConvertToDateTimeZoneCorrectly() {
    val location = Location()
    location.timeZone = "Asia/Bangkok"
    assertThat(location.getDateTimeZone())
        .isEqualTo(DateTimeZone.forID("Asia/Bangkok"))
  }
}