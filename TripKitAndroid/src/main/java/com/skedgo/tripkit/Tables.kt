package com.skedgo.tripkit

import com.skedgo.sqlite.DatabaseField
import com.skedgo.sqlite.DatabaseTable


internal object Tables {
    val FIELD_JSON: DatabaseField = DatabaseField("json", "TEXT")
    @JvmField
    val TRANSPORT_MODES: DatabaseTable = DatabaseTable(
        "transport_modes",
        arrayOf(FIELD_JSON)
    )
    @JvmField
    val REGIONS: DatabaseTable = DatabaseTable(
        "regions",
        arrayOf(FIELD_JSON)
    )
}