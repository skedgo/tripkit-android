package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.net.Uri
import io.reactivex.Observable
import io.reactivex.functions.Function

private val MTAXI_PACKAGE = "au.com.mtaxi"

internal class MTaxiBookingResolver(
    private val isPackageInstalled: Function<String, Boolean>,
    private val getAppIntent: Function<String, Intent>
) : com.skedgo.tripkit.bookingproviders.BookingResolver {

    override fun performExternalActionAsync(params: com.skedgo.tripkit.ExternalActionParams): Observable<com.skedgo.tripkit.BookingAction> {
        val actionBuilder = com.skedgo.tripkit.BookingAction.builder()
        actionBuilder.bookingProvider(com.skedgo.tripkit.bookingproviders.BookingResolver.MTAXI)
        if (isPackageInstalled.apply(MTAXI_PACKAGE)) {
            val action = actionBuilder.hasApp(true).data(
                getAppIntent.apply(MTAXI_PACKAGE)
            ).build()
            return Observable.just(action)
        } else {
            val data = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + MTAXI_PACKAGE))
            val action = actionBuilder
                .hasApp(false)
                .data(data)
                .build()
            return Observable.just(action)
        }
    }

    override fun getTitleForExternalAction(externalAction: String): String? {
        return null
    }


}