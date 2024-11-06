package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.net.Uri
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.geocoding.ReverseGeocodable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal class FlitWaysBookingResolver(private val geocoderFactory: ReverseGeocodable) :
    BookingResolver {

    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        val actionBuilder = BookingAction.builder()
            .bookingProvider(BookingResolver.FLITWAYS)
        val flitWaysPartnerKey = params.flitWaysPartnerKey()
        if (flitWaysPartnerKey == null) {
            val data = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://flitways.com"))
            val action = actionBuilder
                .hasApp(false)
                .data(data)
                .build()
            return Observable.just(action)
        } else {
            // See https://flitways.com/deeplink.
            val segment = params.segment()
            val departure = segment.from
            val arrival = segment.to
            val startTimeInSecs = segment.startTimeInSecs
            val timeZone = segment.timeZone
            return Observable
                .fromCallable {
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US)
                    if (timeZone != null) {
                        dateFormat.timeZone = TimeZone.getTimeZone(timeZone)
                    }

                    val tripDate = dateFormat.format(Date(startTimeInSecs * 1000))
                    "https://flitways.com/api/link".toHttpUrl().newBuilder()
                        .addQueryParameter("trip_date", tripDate)
                        .addQueryParameter("key", flitWaysPartnerKey)
                }
                .flatMap<BookingAction>(Function<HttpUrl.Builder, Observable<BookingAction>> { builder: HttpUrl.Builder ->
                    Observable.combineLatest<String?, String?, BookingAction>(
                        geocoderFactory.getAddress(departure!!.lat, departure.lon),
                        geocoderFactory.getAddress(arrival!!.lat, arrival.lon),
                        BiFunction<String?, String?, BookingAction> { departureAddress: String?, arrivalAddress: String? ->
                            val url: String = builder
                                .addQueryParameter("pickup", departureAddress)
                                .addQueryParameter("destination", arrivalAddress)
                                .build()
                                .toString()
                            actionBuilder
                                .hasApp(false)
                                .data(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                .build()
                        }
                    )
                } as Function<HttpUrl.Builder, Observable<BookingAction>>?)
                .subscribeOn(Schedulers.io())
        }
    }

    override fun getTitleForExternalAction(externalAction: String): String {
        return "Book with FlitWays" // TODO: i18n.
    }
}