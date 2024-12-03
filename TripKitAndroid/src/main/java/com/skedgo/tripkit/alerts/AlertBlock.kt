package com.skedgo.tripkit.alerts

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.common.model.realtimealert.RealtimeAlert
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersAlertBlock::class
)
interface AlertBlock {
    fun alert(): RealtimeAlert?

    fun disruptionType(): String?

    fun operators(): Array<String>

    fun routes(): Array<Route>

    fun modeInfo(): ModeInfo?

    fun stopCodes(): Array<String>

    fun serviceTripIDs(): Array<String>
}