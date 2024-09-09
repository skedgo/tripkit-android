package com.skedgo.tripkit.booking;

import com.skedgo.tripkit.common.model.Region;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * @see <a href="http://skedgo.github.io/tripgo-api/swagger/?url=http://skedgo.github.io/tripgo-api/auth.swagger.yaml#/Auth">Auth APIs</a>
 */
public interface AuthApi {
    /**
     * Returns a list of available providers for a region.
     * Unless you would like to work directly with the auth apis, refer to
     * {@link AuthService#fetchProvidersByRegionAsync(Region, String)} for an easier usage.
     *
     * @param url Should be in form of 'auth/{regionName}'.
     *            A sample url can be like: 'https://granduni.buzzhives.com/satapp-beta/auth/US_CO_Denver'.
     */
    @GET
    Observable<List<AuthProvider>> fetchProvidersAsync(@Url HttpUrl url);

    /**
     * @param url This might be obtained by {@link AuthProvider#url()} when {@link AuthProvider#action()} is 'signin'.
     *            A sample url can be like: 'https://granduni.buzzhives.com/satapp-beta/auth/flitways/signin'.
     */
    @GET
    Observable<BookingForm> signInAsync(@Url String url);

    /**
     * Removes the data saved for the user to the given provider.
     *
     * @param url This might be obtained by {@link AuthProvider#url()} when {@link AuthProvider#action()} is 'logout'.
     *            A sample url can be like: 'https://granduni.buzzhives.com/satapp-beta/auth/lyft/logout'.
     */
    @GET
    Observable<LogOutResponse> logOutAsync(@Url String url);
}