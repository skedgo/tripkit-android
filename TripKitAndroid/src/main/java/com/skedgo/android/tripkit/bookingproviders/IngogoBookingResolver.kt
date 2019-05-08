package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import android.content.res.Resources
import android.net.Uri

import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import com.skedgo.android.tripkit.R
import com.skedgo.android.tripkit.bookingproviders.BookingResolver

import rx.Observable
import rx.functions.Func1

private const val INGOGO_PACKAGE = "com.ingogo.passenger"

internal class IngogoBookingResolver(
    private val resources: Resources,
    private val isPackageInstalled: Func1<String, Boolean>,
    private val getAppIntent: Func1<String, Intent>
) : BookingResolver {

  override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
    val actionBuilder = BookingAction.builder()
    actionBuilder.bookingProvider(BookingResolver.INGOGO)
    return if (isPackageInstalled.call(INGOGO_PACKAGE)) {
      val action = actionBuilder.hasApp(true).data(
          getAppIntent.call(INGOGO_PACKAGE)
      ).build()
      Observable.just(action)
    } else {
      val data = Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=$INGOGO_PACKAGE"))
      val action = actionBuilder
          .hasApp(false)
          .data(data)
          .build()
      Observable.just(action)
    }
  }

  override fun getTitleForExternalAction(externalAction: String): String? {
    return resources.getString(R.string.ingogo_a_taxi)
  }
}