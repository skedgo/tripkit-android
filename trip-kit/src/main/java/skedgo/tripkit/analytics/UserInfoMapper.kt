package skedgo.tripkit.analytics

import skedgo.tripkit.analytics.UserInfo

fun UserInfo.convertToMap(): Map<String, Any> {
  return mapOf<String, Any>(
      "source" to this.source as Any,
      "choiceSet" to this.choiceSet as Any
  )
}