package com.skedgo.tripkit.parkingspots.models

import com.skedgo.tripkit.utils.OptionalCompat

class PricingEntry(val duration: OptionalCompat<Int>, val label: String, val price: Float)