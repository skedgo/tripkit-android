package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCQueryInterface

class GCQuery(
    override var queryText: String,
    override var bounds: GCBoundingBox
) : GCQueryInterface
