package com.skedgo.android.tripkit.bookingproviders

import android.content.res.Resources
import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import com.skedgo.android.tripkit.R
import rx.Observable

private const val INGOGO_PACKAGE = "com.ingogo.passenger"

internal class IngogoBookingResolver(
    private val resources: Resources,
    private val simpleOpenAppBookingResolver: SimpleOpenAppBookingResolver
) : BookingResolver {

  override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> =
      simpleOpenAppBookingResolver.getExternalAction(BookingResolver.INGOGO, INGOGO_PACKAGE)

  override fun getTitleForExternalAction(externalAction: String): String? {
    return resources.getString(R.string.ingogo_a_taxi)
  }
}