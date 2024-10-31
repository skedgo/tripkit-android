package com.skedgo.tripkit.booking

import com.skedgo.tripkit.common.model.region.Region
import io.reactivex.Observable
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class AuthServiceImpl(private val api: AuthApi) : AuthService {

    override fun fetchProvidersByRegionAsync(
        region: Region,
        mode: String?,
        bsb: Boolean
    ): Observable<List<AuthProvider>> {
        return Observable.fromIterable(region.getURLs())
            .concatMapDelayError { url ->
                val builder = url.toHttpUrlOrNull()!!.newBuilder()
                    .addPathSegment("auth")
                    .addPathSegment(region.name.orEmpty())

                mode?.let {
                    builder.addQueryParameter("mode", it)
                }

                if (bsb) {
                    builder.addQueryParameter("bsb", "1")
                }

                fetchProvidersAsync(builder.build())
            }
            .firstElement()
            .toObservable()
    }

    override fun fetchProvidersAsync(url: HttpUrl): Observable<List<AuthProvider>> {
        return api.fetchProvidersAsync(url)
    }

    override fun signInAsync(url: String): Observable<BookingForm> {
        return api.signInAsync(url)
    }

    override fun logOutAsync(url: String): Observable<LogOutResponse> {
        return api.logOutAsync(url)
    }
}