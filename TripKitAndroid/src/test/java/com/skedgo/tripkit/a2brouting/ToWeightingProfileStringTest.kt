package com.skedgo.tripkit.a2brouting

import com.skedgo.tripkit.common.model.Query
import com.skedgo.tripkit.TripKitAndroidRobolectricTest
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class ToWeightingProfileStringTest : TripKitAndroidRobolectricTest() {
  @Test fun shouldConvertToWeightingProfileStringCorrectly() {
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