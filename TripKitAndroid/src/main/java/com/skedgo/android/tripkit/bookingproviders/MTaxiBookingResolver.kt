package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import android.net.Uri
import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import rx.Observable
import rx.functions.Func1

private val MTAXI_PACKAGE = "au.com.mtaxi"

internal class MTaxiBookingResolver(private val isPackageInstalled: Func1<String, Boolean>,
                                    private val getAppIntent: Func1<String, Intent> ) : BookingResolver {

  override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
    val actionBuilder = BookingAction.builder()
    actionBuilder.bookingProvider(BookingResolver.MTAXI)
    if (isPackageInstalled.call(MTAXI_PACKAGE)) {
      val action = actionBuilder.hasApp(true).data(
          getAppIntent.call(MTAXI_PACKAGE)
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