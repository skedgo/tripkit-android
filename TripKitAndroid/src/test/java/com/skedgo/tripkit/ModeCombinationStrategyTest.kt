package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.common.model.TransportMode
import io.mockk.MockKAnnotations
import org.assertj.core.api.Java6Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.util.Sets
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.ArrayList

@RunWith(AndroidJUnit4::class)
class ModeCombinationStrategyTest: MockKTest() {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initRx()
    }

    @After
    fun teardown() {
        tearDownRx()
    }

    @Test
    fun `should deal with modes not found`() {
        val modeIdSets = ModeCombinationStrategy().apply(
            emptyMap<String, TransportMode>(),
            listOf("a", "b")
        )
        assertThat(modeIdSets).hasSize(3).containsExactlyInAnyOrder(
            mutableSetOf("a"),
            mutableSetOf("b"),
            mutableSetOf("a", "b")
        )
    }

    @Test
    fun `should combine modes correctly`() {
        val modeIdSets = ModeCombinationStrategy().apply(
            createSampleModeMap(),
            listOf(
                "pt_pub",
                "pt_sch",
                "ps_tax",
                "ps_shu",
                "me_car",
                "me_car-s_CND",
                "me_car-s_GOG",
                "me_mot",
                "cy_bic",
                "wa_wal"
            )
        )
        assertThat(modeIdSets)
            .describedAs("Should combine modes correctly")
            .isNotNull
            .containsExactlyInAnyOrder(
                mutableSetOf("pt_pub", "pt_sch"),
                mutableSetOf("cy_bic-s_AUSTIN", "ps_shu", "ps_tax"),
                mutableSetOf("me_car"),
                mutableSetOf("me_car-s_CND"),
                mutableSetOf("me_car-s_GOG"),
                mutableSetOf("me_mot"),
                mutableSetOf("cy_bic"),
                mutableSetOf("wa_wal"),
                mutableSetOf("me_car", "pt_sch", "ps_shu", "me_car-s_CND", "cy_bic", "me_mot", "me_car-s_GOG", "pt_pub", "ps_tax")
            )
    }

    private fun createSampleModeMap(): Map<String, TransportMode> {
        val modeMap = mutableMapOf<String, TransportMode>()
        modeMap["pt_pub"] = TransportMode()
        modeMap["ps_tax"] = TransportMode()
        modeMap["me_car"] = TransportMode()
        modeMap["me_car-s_CND"] = TransportMode()
        modeMap["me_car-s_GOG"] = TransportMode()
        modeMap["me_mot"] = TransportMode()
        modeMap["cy_bic"] = TransportMode()
        modeMap["wa_wal"] = TransportMode()

        val schoolBusMode = TransportMode().apply {
            implies = ArrayList(listOf("pt_pub"))
        }
        modeMap["pt_sch"] = schoolBusMode

        val shuttleMode = TransportMode().apply {
            implies = ArrayList(listOf("ps_tax", "cy_bic-s_AUSTIN"))
        }
        modeMap["ps_shu"] = shuttleMode

        return modeMap
    }
}