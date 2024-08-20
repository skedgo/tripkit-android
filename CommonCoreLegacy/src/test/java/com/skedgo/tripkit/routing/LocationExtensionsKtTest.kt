package com.skedgo.tripkit.routing

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.model.Location
import org.assertj.core.api.Java6Assertions.assertThat
import org.joda.time.DateTimeZone
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationExtensionsKtTest {
    @Test
    fun shouldConvertToDateTimeZoneCorrectly() {
        val location = Location()
        location.timeZone = "Asia/Bangkok"
        assertThat(location.dateTimeZone)
            .isEqualTo(DateTimeZone.forID("Asia/Bangkok"))
    }
}