package com.skedgo.tripkit.a2brouting

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.TripKitAndroidRobolectricTest
import com.skedgo.tripkit.common.model.Query
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToWeightingProfileStringTest : TripKitAndroidRobolectricTest() {
    @Test
    fun shouldConvertToWeightingProfileStringCorrectly() {
        val query = Query().apply {
            budgetWeight = 50
            timeWeight = 50
            hassleWeight = 50
            environmentWeight = 50
        }
        ToWeightingProfileString
            .toWeightingProfileString(query)
            .shouldEqualTo("(1.0,1.0,1.0,1.0)")
    }
}