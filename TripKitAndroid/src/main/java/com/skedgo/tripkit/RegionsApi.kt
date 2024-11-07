package com.skedgo.tripkit

import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.region.RegionsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface RegionsApi {
    @POST("regions.json")
    fun fetchRegionsAsyncAsSingle(@Body bodyContent: RequestBodyContent): Single<RegionsResponse>

    @POST("regions.json")
    fun fetchRegionsAsync(@Body bodyContent: RequestBodyContent): Observable<RegionsResponse>

    class RequestBodyContent(
        @SerializedName("v") private val apiVersion: Int,
        @SerializedName("app") private val appFlavr: String?
    )
}