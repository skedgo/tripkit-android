package com.skedgo.tripkit.parkingspots.models

import com.gojuno.koptional.Optional

class PricingTable(
    val currency: String,
    val currencySymbol: String,
    val entries: List<PricingEntry>,
    val title: String,
    val subtitle: Optional<String>
)