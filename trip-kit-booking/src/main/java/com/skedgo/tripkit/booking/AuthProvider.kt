package com.skedgo.tripkit.booking

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersAuthProvider::class
)
interface AuthProvider {
    fun modeIdentifier(): String?

    fun provider(): String?

    fun action(): String?

    fun url(): String?

    fun actionTitle(): String?

    fun status(): String?

    fun companyInfo(): CompanyInfo?
}