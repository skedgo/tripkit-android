package skedgo.tripkit.parkingspots.models

import org.joda.time.LocalTime

class OpeningHour(val day: String, val `open`: LocalTime, val close: LocalTime)