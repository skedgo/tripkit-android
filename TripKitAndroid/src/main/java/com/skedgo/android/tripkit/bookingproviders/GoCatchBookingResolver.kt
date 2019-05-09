package com.skedgo.android.tripkit.bookingproviders

import android.content.res.Resources
import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import com.skedgo.android.tripkit.R
import rx.Observable

private const val GOCATCH_PACKAGE = "com.gocatchapp.goCatch"

internal class GoCatchBookingResolver(
    private val resources: Resources,
    private val simpleOpenAppBookingResolver: SimpleOpenAppBookingResolver
) : BookingResolver {

  override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> =
      simpleOpenAppBookingResolver.getExternalAction(BookingResolver.GOCATCH, GOCATCH_PACKAGE)

  override fun getTitleForExternalAction(externalAction: String): String? {
    return resources.getString(R.string.gocatch_a_taxi)
  }
}