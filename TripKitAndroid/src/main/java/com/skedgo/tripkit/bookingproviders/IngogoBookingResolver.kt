package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.R
import io.reactivex.Observable
import io.reactivex.functions.Function

internal class IngogoBookingResolver(
    private val resources: Resources,
    private val isPackageInstalled: Function<String, Boolean>
) : BookingResolver {
    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        val actionBuilder = BookingAction.builder()
        actionBuilder.bookingProvider(BookingResolver.INGOGO)
        try {
            if (isPackageInstalled.apply(INGOGO_PACKAGE)) {
                val action = actionBuilder.hasApp(true).data(
                    Intent(Intent.ACTION_VIEW).setData(Uri.parse("ingogo://"))
                ).build()
                return Observable.just(action)
            } else {
                val data = Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=$INGOGO_PACKAGE"))
                val action = actionBuilder
                    .hasApp(false)
                    .data(data)
                    .build()
                return Observable.just(action)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Observable.empty()
        }
    }

    override fun getTitleForExternalAction(externalAction: String): String {
        return resources.getString(R.string.ingogo_a_taxi)
    }

    companion object {
        private const val INGOGO_PACKAGE = "com.ingogo.passenger"
    }
}