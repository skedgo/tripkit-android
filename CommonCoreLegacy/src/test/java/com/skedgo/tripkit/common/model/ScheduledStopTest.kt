package com.skedgo.tripkit.common.model

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.model.stop.ScheduledStop
import com.skedgo.tripkit.common.model.stop.StopType.BUS
import com.skedgo.tripkit.common.util.Gsons.createForLowercaseEnum
import com.skedgo.tripkit.routing.ModeInfo
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScheduledStopTest {
    @Test
    fun parseFromJson() {
        val json = """{
          "stopType": "train",
          "name": "Blackheath Station",
          "class": "StopLocation",
          "lng": 150.2844,
          "stopCode": "AU_NSW_Sydney-278510",
          "code": "AU_NSW_Sydney-278510",
          "modeInfo": {
            "alt": "train",
            "localIcon": "train"
          },
          "lat": -33.6336,
          "popularity": 3465
        }"""
        val stop = createForLowercaseEnum().fromJson(json, ScheduledStop::class.java)
        Java6Assertions.assertThat(stop).isNotNull()
        Java6Assertions.assertThat(stop.modeInfo).isNotNull()
        Java6Assertions.assertThat(stop.modeInfo!!.alternativeText).isEqualTo("train")
        Java6Assertions.assertThat(stop.modeInfo!!.localIconName).isEqualTo("train")
    }

    @Test
    fun parcel() {
        val expected = ScheduledStop()
        expected.code = "AU_NSW_Sydney-278510"
        expected.name = "Blackheath Station"
        expected.lat = -33.6336
        expected.lon = 150.2844
        expected.type = BUS

        val modeInfo = ModeInfo()
        modeInfo.alternativeText = "train"
        modeInfo.localIconName = "train"
        expected.modeInfo = modeInfo

        val parcel = Parcel.obtain()
        expected.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val actual = ScheduledStop.CREATOR.createFromParcel(parcel)

        Java6Assertions.assertThat(actual.code).isEqualTo(expected.code)
        Java6Assertions.assertThat(actual.name).isEqualTo(expected.name)
        Java6Assertions.assertThat(actual.lat).isEqualTo(expected.lat)
        Java6Assertions.assertThat(actual.lon).isEqualTo(expected.lon)
        Java6Assertions.assertThat(actual.modeInfo).isNotNull()
        Java6Assertions.assertThat(actual.modeInfo!!.alternativeText)
            .isEqualTo(expected.modeInfo!!.alternativeText)
        Java6Assertions.assertThat(actual.modeInfo!!.localIconName)
            .isEqualTo(expected.modeInfo!!.localIconName)
    }
}