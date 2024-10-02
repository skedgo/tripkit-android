package com.skedgo.tripkit.common.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersMiniInstruction::class
)
abstract class MiniInstruction {
    @get:SerializedName("description")
    abstract val description: String?

    @get:SerializedName("instruction")
    abstract val instruction: String?

    @get:SerializedName("mainValue")
    abstract val mainValue: String?
}