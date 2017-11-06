package com.skedgo.android.tripkit.servererror

enum class ServerError(val code: Int) {
  InternalServerError(500),
  UnknownHost(-1),
  Other(0)
}