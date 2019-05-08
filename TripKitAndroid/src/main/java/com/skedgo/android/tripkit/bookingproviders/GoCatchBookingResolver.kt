package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import android.content.res.Resources
import android.net.Uri

import com.skedgo.android.common.model.Location
import skedgo.tripkit.routing.TripSegment
import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import skedgo.tripkit.geocoding.ReverseGeocodable
import com.skedgo.android.tripkit.R

import rx.Observable
import rx.functions.Func1

private const val GOCATCH_PACKAGE = "com.gocatchapp.goCatch"

internal class GoCatchBookingResolver(
    private val resources: Resources,
    private val isPackageInstalled: Func1<String, Boolean>,
    private val getAppIntent: Func1<String, Intent>
) : BookingResolver {

  override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
    val actionBuilder = BookingAction.builder()
    actionBuilder.bookingProvider(BookingResolver.GOCATCH)
    return if (isPackageInstalled.call(GOCATCH_PACKAGE)) {
      val action = actionBuilder.hasApp(true).data(
          getAppIntent.call(GOCATCH_PACKAGE)
      ).build()
      Observable.just(action)
    } else {
      val data = Intent(Intent.ACTION_VIEW)
          .setData(Uri.parse("https://play.google.com/store/apps/details?id=$GOCATCH_PACKAGE"))
      val action = BookingAction.builder()
          .bookingProvider(BookingResolver.GOCATCH)
          .hasApp(false)
          .data(data)
          .build()
      Observable.just(action)
    }
  }

  override fun getTitleForExternalAction(externalAction: String): String? {
    return resources.getString(R.string.gocatch_a_taxi)
  }
}