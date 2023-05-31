package com.skedgo.tripkit.data.clients

import com.skedgo.tripkit.account.data.Client
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface ClientsApi {

    @GET
    fun getClients(@Url url: String): Single<List<Client>>

}