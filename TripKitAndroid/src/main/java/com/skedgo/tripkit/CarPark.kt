package com.skedgo.tripkit

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersCarPark::class
)
abstract class CarPark {
    abstract fun identifier(): String

    abstract fun name(): String

    abstract fun totalSpaces(): Int

    abstract fun availableSpaces(): Int

    abstract fun lastUpdate(): Long
}