package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.R
import io.reactivex.Observable
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

internal class TelBookingResolver(private val resources: Resources) : BookingResolver {
    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        var telAction = params.action()
        if (telAction.contains("?name=")) {
            telAction = telAction.substring(0, telAction.indexOf("?name="))
        }
        val action = BookingAction.builder()
            .bookingProvider(BookingResolver.OTHERS)
            .hasApp(false)
            .data(Intent(Intent.ACTION_VIEW, Uri.parse(telAction)))
            .build()
        return Observable.just(action)
    }

    override fun getTitleForExternalAction(externalAction: String): String? {
        if (externalAction.contains("name=")) {
            var name: String? =
                externalAction.substring(externalAction.indexOf("name=") + "name=".length)
            try {
                name = URLDecoder.decode(name, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
            }
            return resources.getString(R.string.call__pattern, name)
        } else {
            return resources.getString(R.string.call)
        }
    }
}