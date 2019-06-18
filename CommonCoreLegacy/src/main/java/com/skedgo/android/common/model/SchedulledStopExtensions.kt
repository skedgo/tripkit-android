package com.skedgo.android.common.model

fun ScheduledStop.getEmbarkationStopCode(): List<String> {
  return listOf(code).plus(children.orEmpty().map { it.code })
}
