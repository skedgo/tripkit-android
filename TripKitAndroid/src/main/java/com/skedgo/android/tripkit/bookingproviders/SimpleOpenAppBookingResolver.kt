package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import android.net.Uri
import com.skedgo.android.tripkit.BookingAction
import rx.Observable
import rx.functions.Func1

internal class SimpleOpenAppBookingResolver(
    private val isPackageInstalled: Func1<String, Boolean>,
    private val getAppIntent: Func1<String, Intent>
) {

  fun getExternalAction(bookingResolver: Int, packageName: String): Observable<BookingAction> {
    val actionBuilder = BookingAction.builder()
    actionBuilder.bookingProvider(bookingResolver)
    return if (isPackageInstalled.call(packageName)) {
      val action = actionBuilder.hasApp(true).data(
          getAppIntent.call(packageName)
      ).build()
      Observable.just(action)
    } else {
      val data = Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
      val action = actionBuilder
          .hasApp(false)
          .data(data)
          .build()
      Observable.just(action)
    }
  }
}