package com.skedgo.tripkit

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersLocationInfoDetails::class
)
abstract class LocationInfoDetails {
    abstract fun w3w(): String?

    abstract fun w3wInfoURL(): String?
}