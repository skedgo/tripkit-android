package com.skedgo.tripkit.booking

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skedgo.tripkit.booking.viewmodel.AuthenticationViewModel
import com.skedgo.tripkit.booking.viewmodel.AuthenticationViewModelImpl
import com.skedgo.tripkit.booking.viewmodel.BookingViewModel
import com.skedgo.tripkit.booking.viewmodel.BookingViewModelImpl
import com.skedgo.tripkit.configuration.ServerManager
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
class BookingModule {

    @Provides
    fun bookingApi(httpClient: OkHttpClient): BookingApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(FormField::class.java, FormFieldJsonAdapter())
            .create()
        return Builder()
            /* This base url is ignored as the api relies on @Url. */
            .baseUrl(ServerManager.configuration.apiTripGoUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(BookingApi::class.java)
    }

    @Provides
    fun quickBookingApi(httpClient: OkHttpClient): QuickBookingApi {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(GsonAdaptersQuickBooking())
            .create()
        return Builder()
            /* This base url is ignored as the api relies on @Url. */
            .baseUrl(ServerManager.configuration.apiTripGoUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(QuickBookingApi::class.java)
    }

    @Provides
    fun authApi(httpClient: OkHttpClient): AuthApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(FormField::class.java, FormFieldJsonAdapter())
            .registerTypeAdapterFactory(GsonAdaptersAuthProvider())
            .registerTypeAdapterFactory(GsonAdaptersLogOutResponse())
            .create()
        return Builder()
            /* This base url is ignored as the api relies on @Url. */
            .baseUrl(ServerManager.configuration.apiTripGoUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    fun authService(authApi: AuthApi): AuthService {
        return AuthServiceImpl(authApi)
    }

    @Provides
    fun bookingViewModel(bookingService: BookingService): BookingViewModel {
        return BookingViewModelImpl(bookingService)
    }

    @Provides
    fun authenticationViewModel(): AuthenticationViewModel {
        return AuthenticationViewModelImpl()
    }

    @Provides
    fun provideExternalOAuthServiceGenerator(): ExternalOAuthServiceGenerator {
        return ExternalOAuthServiceGenerator(OkHttpClient.Builder())
    }

    @Provides
    fun getExternalOAuthService(externalOAuthServiceGenerator: ExternalOAuthServiceGenerator): ExternalOAuthService {
        return ExternalOAuthServiceImpl(externalOAuthServiceGenerator)
    }

    @Provides
    fun getBookingService(bookingApi: BookingApi): BookingService {
        return BookingServiceImpl(bookingApi, Gson())
    }

    @Provides
    fun getQuickBookingService(quickBookingApi: QuickBookingApi): QuickBookingService {
        return QuickBookingServiceImpl(quickBookingApi)
    }
}
