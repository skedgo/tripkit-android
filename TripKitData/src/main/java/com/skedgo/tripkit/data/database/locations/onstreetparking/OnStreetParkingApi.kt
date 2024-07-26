package com.skedgo.tripkit.data.database.locations.onstreetparking

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OnStreetParkingApi {

    @GET("locationInfo.json?app=beta")
    fun fetchLocationInfoAsync(
        @Query("identifier") identifier: String
    ): Single<OnStreetParkingLocationDto>
}