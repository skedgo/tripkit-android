package com.skedgo.tripkit.routing

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripGroupExtensionsKtTest {

    @Test
    fun `should be child mode`() {
        "cy_bic-s_Melbourne_BikeShare".isChildModeOf("cy_bic-s").`should be true`()
        "pt_pub_train".isChildModeOf("pt_pub").`should be true`()
    }

    @Test
    fun `should not be child mode`() {
        "cy_bic-s_Melbourne_BikeShare".isChildModeOf("cy_bic").`should be false`()
    }
}