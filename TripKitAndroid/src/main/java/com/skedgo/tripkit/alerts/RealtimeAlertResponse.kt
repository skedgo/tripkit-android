package com.skedgo.tripkit.alerts

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersRealtimeAlertResponse::class
)
interface RealtimeAlertResponse {
    fun alerts(): List<AlertBlock>
}