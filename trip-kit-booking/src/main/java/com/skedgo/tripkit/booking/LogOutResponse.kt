package com.skedgo.tripkit.booking

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersLogOutResponse::class
)
abstract class LogOutResponse {
    /**
     * Indicates whether user data was removed or not.
     */
    @Default
    open fun changed(): Boolean {
        return false
    }
}