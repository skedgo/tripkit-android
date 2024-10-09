package com.skedgo.tripkit.booking

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersCompanyInfo::class
)
interface CompanyInfo {
    fun name(): String?

    fun website(): String?
}