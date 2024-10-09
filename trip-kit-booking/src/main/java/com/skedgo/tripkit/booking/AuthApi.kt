package com.skedgo.tripkit.booking

import io.reactivex.Observable
import okhttp3.HttpUrl
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @see [Auth APIs](http://skedgo.github.io/tripgo-api/swagger/?url=http://skedgo.github.io/tripgo-api/auth.swagger.yaml./Auth)
 */
interface AuthApi {
    /**
     * Returns a list of available providers for a region.
     * Unless you would like to work directly with the auth apis, refer to
     * [AuthService.fetchProvidersByRegionAsync] for an easier usage.
     *
     * @param url Should be in form of 'auth/{regionName}'.
     * A sample url can be like: 'https://granduni.buzzhives.com/satapp-beta/auth/US_CO_Denver'.
     */
    @GET
    fun fetchProvidersAsync(@Url url: HttpUrl): Observable<List<AuthProvider>>

    /**
     * @param url This might be obtained by [AuthProvider.url] when [AuthProvider.action] is 'signin'.
     * A sample url can be like: 'https://granduni.buzzhives.com/satapp-beta/auth/flitways/signin'.
     */
    @GET
    fun signInAsync(@Url url: String): Observable<BookingForm>

    /**
     * Removes the data saved for the user to the given provider.
     *
     * @param url This might be obtained by [AuthProvider.url] when [AuthProvider.action] is 'logout'.
     * A sample url can be like: 'https://granduni.buzzhives.com/satapp-beta/auth/lyft/logout'.
     */
    @GET
    fun logOutAsync(@Url url: String): Observable<LogOutResponse>
}