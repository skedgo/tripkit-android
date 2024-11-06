package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.R
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.geocoding.ReverseGeocodable
import io.reactivex.Observable
import io.reactivex.functions.Function

internal class GoCatchBookingResolver(
    private val resources: Resources,
    private val isPackageInstalled: Function<String, Boolean>,
    private val geocoderFactory: ReverseGeocodable
) : BookingResolver {
    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        try {
            if (isPackageInstalled.apply(GOCATCH_PACKAGE)) {
                val segment = params.segment()
                val departure = segment.from ?: Location(0.0,0.0)
                val arrival = segment.to ?: Location(0.0,0.0)
                return geocoderFactory.getAddress(arrival.lat, arrival.lon)
                    .map { arrivalAddress ->
                        val uri = Uri.parse("gocatch://referral")
                            .buildUpon()
                            .appendQueryParameter("code", GOCATCH_CODE)
                            .appendQueryParameter("destination", arrivalAddress)
                            .appendQueryParameter("pickup", "")
                            .appendQueryParameter("lat", departure!!.lat.toString())
                            .appendQueryParameter("lng", departure.lon.toString())
                            .build()
                        BookingAction.builder()
                            .bookingProvider(BookingResolver.GOCATCH)
                            .hasApp(true)
                            .data(Intent(Intent.ACTION_VIEW).setData(uri))
                            .build()
                    }
            } else {
                val data = Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=$GOCATCH_PACKAGE"))
                val action = BookingAction.builder()
                    .bookingProvider(BookingResolver.GOCATCH)
                    .hasApp(false)
                    .data(data)
                    .build()
                return Observable.just(action)
            }
        } catch (e: Exception) {
            return Observable.empty()
        }
    }

    override fun getTitleForExternalAction(externalAction: String): String {
        return resources.getString(R.string.gocatch_a_taxi)
    }

    companion object {
        private const val GOCATCH_PACKAGE = "com.gocatchapp.goCatch"
        private const val GOCATCH_CODE = "tripgo"
    }
}