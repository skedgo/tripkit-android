package com.skedgo.tripkit.data.database.locations.freefloating

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersFreeFloatingOperator::class)
interface FreeFloatingOperator {
    fun name(): String?
    fun phone(): String?
    fun website(): String?
    fun appInfo(): FreeFloatingAppInfo?
}