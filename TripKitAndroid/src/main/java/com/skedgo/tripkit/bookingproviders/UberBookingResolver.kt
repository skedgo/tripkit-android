package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.net.Uri
import io.reactivex.Observable
import io.reactivex.functions.Function

internal const val UBER_PACKAGE = "com.ubercab"

class UberBookingResolver(
    private val isPackageInstalled: Function<String, Boolean>,
    private val getAppIntent: Function<String, Intent>
) : com.skedgo.tripkit.bookingproviders.BookingResolver {
    override fun performExternalActionAsync(params: com.skedgo.tripkit.ExternalActionParams): Observable<com.skedgo.tripkit.BookingAction> {
        val actionBuilder = com.skedgo.tripkit.BookingAction.builder()
        actionBuilder.bookingProvider(com.skedgo.tripkit.bookingproviders.BookingResolver.UBER)

        if (isPackageInstalled.apply(UBER_PACKAGE)) {
            val intent = getAppIntent.apply(UBER_PACKAGE)
            val segment = params.segment()

            intent.data = Uri.parse(
                "uber://?action=setPickup" +
                    "&pickup[latitude]=${segment.from?.lat}" +
                    "&pickup[longitude]=${segment.from?.lon}" +
                    "&dropoff[latitude]=${segment.to?.lat}" +
                    "&dropoff[longitude]=${segment.to?.lon}"
            )

            val action = actionBuilder.hasApp(true).data(intent).build()
            return Observable.just(action)
        } else {
            val data = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://play.google.com/store/apps/details?id=$UBER_PACKAGE"))
            val action = actionBuilder
                .hasApp(false)
                .data(data)
                .build()
            return Observable.just(action)
        }
    }

    override fun getTitleForExternalAction(externalAction: String): String? = null
}