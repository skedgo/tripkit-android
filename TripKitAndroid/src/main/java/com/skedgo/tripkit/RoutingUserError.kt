package com.skedgo.tripkit

open class RoutingError(detailMessage: String) : RuntimeException(detailMessage)

class RoutingUserError(detailMessage: String) : RoutingError(detailMessage)

class NoConnectionError(detailMessage: String) : RoutingError(detailMessage)