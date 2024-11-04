package com.skedgo.tripkit.booking.mybookings

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersMyBookingsResponse::class
)
abstract class MyBookingsResponse {
    abstract fun bookings(): List<MyBookingsConfirmationResponse?>?

    @Default
    open fun count(): Int {
        return 0
    }
}
