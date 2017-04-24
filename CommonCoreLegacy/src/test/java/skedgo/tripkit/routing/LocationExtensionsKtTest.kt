package skedgo.tripkit.routing

import com.skedgo.android.common.model.Location
import org.assertj.core.api.Assertions.assertThat
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