package com.skedgo.tripkit.common.model

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersBookingConfirmationInput::class)
interface BookingConfirmationInput {
    fun field(): String
    fun type(): String
}