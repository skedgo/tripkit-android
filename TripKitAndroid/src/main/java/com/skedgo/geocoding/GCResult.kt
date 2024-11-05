package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCResultInterface


open class GCResult(
    override var name: String = "",
    override var lat: Double? = null,
    override var lng: Double? = null
) : GCResultInterface
