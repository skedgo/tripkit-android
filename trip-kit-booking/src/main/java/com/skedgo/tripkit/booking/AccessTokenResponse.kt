package com.skedgo.tripkit.booking

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    AccessTokenResponse::class
)
interface AccessTokenResponse {
    @SerializedName("access_token")
    fun accessToken(): String?

    @SerializedName("token_type")
    fun tokenType(): String?

    @SerializedName("expires_in")
    fun expiresIn(): Long

    @SerializedName("refresh_token")
    fun refreshToken(): String?
}
