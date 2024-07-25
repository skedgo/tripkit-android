package com.skedgo.tripkit.common.model

import com.google.gson.Gson
import com.skedgo.tripkit.common.BaseUnitTest
import com.skedgo.tripkit.common.R
import org.amshove.kluent.shouldEqualTo
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import com.skedgo.tripkit.routing.ServiceColor
import java.util.*

@Suppress("IllegalIdentifier")
class TransportModeTest : BaseUnitTest() {
    @Test
    fun `should associate bike-share icon with id starting with "cy_bic-s"`() {
        val localIconResId = TransportMode.getLocalIconResId("cy_bic-s")
        localIconResId.shouldEqualTo(R.drawable.ic_bicycle_share)
    }

    @Test
    fun serializedNames() {
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

    @Test
    fun shouldBeEqual() {
        val mode0 = TransportMode.fromId("bus")
        val mode1 = TransportMode.fromId("bus")

        assertThat(mode0).isEqualTo(mode1)
        assertThat(mode1).isEqualTo(mode0)
    }

    @Test
    fun shouldNotBeEqual() {
        val bus = TransportMode.fromId("bus")
        val car = TransportMode.fromId("car")

        assertThat(bus).isNotEqualTo("Awesome!")
        assertThat(bus).isNotEqualTo(car)
        assertThat(car).isNotEqualTo(bus)
    }

    @Test
    fun newFromId() {
        val bus = TransportMode.fromId("bus")
        assertThat(bus.id).isEqualTo("bus")
    }
}
