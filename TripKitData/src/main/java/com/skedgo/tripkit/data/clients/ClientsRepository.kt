package com.skedgo.tripkit.data.clients

import com.skedgo.tripkit.account.data.Client
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface ClientsRepository {

    fun getClients(url: String): Single<List<Client>>
    fun getClients(): Single<List<Client>>

    class ClientRepositoryImpl @Inject constructor(
        private val api: ClientsApi
    ) : ClientsRepository {
        override fun getClients(url: String): Single<List<Client>> =
            api.getClients(url).subscribeOn(Schedulers.io())


        override fun getClients(): Single<List<Client>> =
            api.getClients().subscribeOn(Schedulers.io())

    }

}