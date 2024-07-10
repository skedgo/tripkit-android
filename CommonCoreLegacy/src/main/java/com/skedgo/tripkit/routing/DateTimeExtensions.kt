package com.skedgo.tripkit.routing

import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

fun DateTime.toSeconds() = TimeUnit.MILLISECONDS.toSeconds(millis)
