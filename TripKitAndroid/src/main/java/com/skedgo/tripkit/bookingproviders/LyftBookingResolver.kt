package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.R
import io.reactivex.Observable
import io.reactivex.functions.Function

internal class LyftBookingResolver(
    private val resources: Resources,
    private val isPackageInstalled: Function<String, Boolean>
) : BookingResolver {
    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        val actionBuilder = BookingAction.builder()
        actionBuilder.bookingProvider(BookingResolver.LYFT)
        try {
            if (isPackageInstalled.apply(LYFT_PACKAGE)) {
                val action = actionBuilder.hasApp(true).data(
                    Intent(Intent.ACTION_VIEW).setData(Uri.parse("lyft://"))
                ).build()
                return Observable.just(action)
            } else {
                val data = Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=$LYFT_PACKAGE"))
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
        try {
            return if (isPackageInstalled.apply(LYFT_PACKAGE)
            ) resources.getString(R.string.open_lyft)
            else resources.getString(R.string.get_lyft)
        } catch (e: Exception) {
            e.printStackTrace()
            return resources.getString(R.string.get_lyft)
        }
    }

    companion object {
        private const val LYFT_PACKAGE = "me.lyft.android"
    }
}