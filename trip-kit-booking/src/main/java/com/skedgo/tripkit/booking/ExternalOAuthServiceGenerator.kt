package com.skedgo.tripkit.booking

import android.util.Base64
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ExternalOAuthServiceGenerator(private val builder: OkHttpClient.Builder) {

    fun createService(
        baseUrl: String,
        username: String?,
        password: String?,
        credentialsInHeader: Boolean
    ): ExternalOAuthApi {
        if (username != null && password != null) {
            val credentials = "$username:$password"
            val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            builder.addInterceptor { chain ->
                val original = chain.request()
                var requestBuilder = original.newBuilder()

                if (credentialsInHeader) {
                    requestBuilder = requestBuilder
                        .header("authorization", basic)
                        .header("accept-encoding", "gzip")
                } else {
                    requestBuilder = requestBuilder
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                }

                requestBuilder.method(original.method, original.body)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }

        val gson = GsonBuilder()
            .registerTypeAdapterFactory(GsonAdaptersAccessTokenResponse())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(builder.build())
            .build()

        return retrofit.create(ExternalOAuthApi::class.java)
    }
}
