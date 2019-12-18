package com.skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Query

object ToWeightingProfileString {
  fun toWeightingProfileString(query: Query): String {
    val price = query.budgetWeight.toProfileWeight()
    val environment = query.environmentWeight.toProfileWeight()
    val duration = query.timeWeight.toProfileWeight()
    val convenience = query.hassleWeight.toProfileWeight()
    return "($price,$environment,$duration,$convenience)"
  }
}
