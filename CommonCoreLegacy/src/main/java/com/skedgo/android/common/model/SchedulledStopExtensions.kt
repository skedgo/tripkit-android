package com.skedgo.android.common.model

fun ScheduledStop.getEmbarkationStopCode(): List<String>? {
  return when {
    isParent && hasChildren() -> children.map { it.code }
    !isParent -> listOf(code)
    else -> null
  }
}