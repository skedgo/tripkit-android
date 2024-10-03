package com.skedgo.tripkit.parkingspots.models

import com.skedgo.tripkit.utils.OptionalCompat

class PricingTable(
    val currency: String,
    val currencySymbol: String,
    val entries: List<PricingEntry>,
    val title: String,
    val subtitle: OptionalCompat<String>
)