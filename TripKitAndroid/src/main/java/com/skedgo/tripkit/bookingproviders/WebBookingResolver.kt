package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.R
import io.reactivex.Observable

internal class WebBookingResolver(private val resources: Resources) : BookingResolver {
    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        val action = BookingAction.builder()
            .bookingProvider(BookingResolver.OTHERS)
            .hasApp(false)
            .data(Intent(Intent.ACTION_VIEW, Uri.parse(params.action())))
            .build()
        return Observable.just(action)
    }

    override fun getTitleForExternalAction(externalAction: String): String? {
        return resources.getString(R.string.show_website)
    }
}