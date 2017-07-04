package skedgo.tripkit.android

import com.skedgo.android.common.model.Query
import skedgo.tripkit.a2brouting.toProfileWeight

object ToWeightingProfileString {
  fun toWeightingProfileString(query: Query): String {
    val price = query.budgetWeight.toProfileWeight()
    val environment = query.environmentWeight.toProfileWeight()
    val duration = query.timeWeight.toProfileWeight()
    val convenience = query.hassleWeight.toProfileWeight()
    return "($price,$environment,$duration,$convenience)"
  }
}
