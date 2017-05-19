package com.skedgo.android.common.model

import com.google.gson.Gson
import com.skedgo.android.common.BaseUnitTest
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import skedgo.tripkit.routing.ServiceColor
import java.util.*

class TransportModeTest : BaseUnitTest() {
  @Test fun serializedNames() {
    val mode = TransportMode()
    mode.id = "Some id"
    mode.setURL("skedgo.com")
    mode.title = "Some title"
    mode.iconId = "Some icon id"
    mode.setDarkIcon("Some dark icon")
    mode.setImplies(ArrayList(Arrays.asList("Aha", "Uhu", "Huh?")))
    mode.setColor(ServiceColor(1, 2, 3))

    val json = Gson().toJsonTree(mode).asJsonObject
    assertThat(json).isNotNull()
    assertThat(json.has("id")).isTrue()
    assertThat(json.has("URL")).isTrue()
    assertThat(json.has("title")).isTrue()
    assertThat(json.has("implies")).isTrue()
    assertThat(json.has("required")).isTrue()
    assertThat(json.has("icon")).isTrue()
    assertThat(json.has("darkIcon")).isTrue()
    assertThat(json.has("color")).isTrue()
  }

  @Test fun shouldBeEqual() {
    val mode0 = TransportMode.fromId("bus")
    val mode1 = TransportMode.fromId("bus")

    assertThat(mode0).isEqualTo(mode1)
    assertThat(mode1).isEqualTo(mode0)
  }

  @Test fun shouldNotBeEqual() {
    val bus = TransportMode.fromId("bus")
    val car = TransportMode.fromId("car")

    assertThat(bus).isNotEqualTo("Awesome!")
    assertThat(bus).isNotEqualTo(car)
    assertThat(car).isNotEqualTo(bus)
  }

  @Test fun newFromId() {
    val bus = TransportMode.fromId("bus")
    assertThat(bus.id).isEqualTo("bus")
  }

  @Test fun parcel() {
    val mode = TransportMode()
    mode.id = "Some id"
    mode.setURL("skedgo.com")
    mode.title = "Some title"
    mode.iconId = "Some icon id"
    mode.setDarkIcon("Some dark icon")
    mode.setImplies(ArrayList(Arrays.asList("Aha", "Uhu", "Huh?")))
    mode.setColor(ServiceColor(1, 2, 3))

    val actual = TransportMode.CREATOR.createFromParcel(Utils.parcel(mode))

    assertThat(actual).isNotNull()
    assertEquals(mode.id, actual.id)
    assertEquals(mode.url, actual.url)
    assertEquals(mode.title, actual.title)
    assertEquals(mode.iconId, actual.iconId)
    assertEquals(mode.darkIcon, actual.darkIcon)
    assertThat(actual.color).isEqualTo(mode.color)
    assertThat(actual.implies).containsExactlyElementsOf(mode.implies)
  }
}