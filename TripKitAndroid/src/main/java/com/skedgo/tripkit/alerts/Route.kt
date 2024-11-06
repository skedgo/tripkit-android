package com.skedgo.tripkit.alerts

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.routing.ModeInfo
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersRoute::class
)
abstract class Route {
    abstract fun id(): String

    abstract fun name(): String?

    abstract fun number(): String?

    abstract fun modeInfo(): ModeInfo?

    @Default
    open fun type(): Int {
        return -1
    }
}
