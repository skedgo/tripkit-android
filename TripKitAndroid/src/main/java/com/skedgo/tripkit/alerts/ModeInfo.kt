package com.skedgo.tripkit.alerts

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.routing.ServiceColor
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersModeInfo::class
)
interface ModeInfo {
    fun color(): ServiceColor?

    fun identifier(): String?

    fun alt(): String?
}