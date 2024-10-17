package com.skedgo.tripkit.booking.mybookings

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.common.model.booking.confirmation.BookingConfirmation
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersMyBookingsConfirmationResponse::class
)
abstract class MyBookingsConfirmationResponse {
    abstract fun confirmation(): BookingConfirmation

    @Default
    open fun index(): Int {
        return 0
    }

    @Default
    open fun time(): Int {
        return 0
    }

    abstract fun trips(): List<String?>?

    abstract fun mode(): String?
}
