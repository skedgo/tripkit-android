package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import android.net.Uri
import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import rx.Observable
import rx.functions.Func1

internal const val UBER_PACKAGE = "com.ubercab"

class UberBookingResolver(
    private val isPackageInstalled: Func1<String, Boolean>,
    private val getAppIntent: Func1<String, Intent>
) : BookingResolver {
  override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
    val actionBuilder = BookingAction.builder()
    actionBuilder.bookingProvider(BookingResolver.UBER)

    if (isPackageInstalled.call(UBER_PACKAGE)) {
      val intent = getAppIntent.call(UBER_PACKAGE)
      val segment = params.segment()

      intent.data = Uri.parse("uber://?action=setPickup" +
          "&pickup[latitude]=${segment.from.lat}" +
          "&pickup[longitude]=${segment.from.lon}" +
          "&dropoff[latitude]=${segment.to.lat}" +
          "&dropoff[longitude]=${segment.to.lon}")

      val action = actionBuilder.hasApp(true).data(intent).build()
      return Observable.just(action)
    } else {
      val data = Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + UBER_PACKAGE))
      val action = actionBuilder
          .hasApp(false)
          .data(data)
          .build()
      return Observable.just(action)
    }
  }

  override fun getTitleForExternalAction(externalAction: String): String? = null
}