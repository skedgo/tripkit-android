package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Resources
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import com.skedgo.tripkit.geocoding.ReverseGeocodable
import io.reactivex.Observable
import io.reactivex.functions.Function

class BookingResolverImpl(
    resources: Resources,
    packageManager: PackageManager,
    geocoderFactory: ReverseGeocodable
) : BookingResolver {
    private val resolverMap: MutableMap<String?, BookingResolver>

    init {
        val isPackageInstalled: Function<String, Boolean> = object : Function<String, Boolean> {
            override fun apply(packageName: String): Boolean {
                try {
                    packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                    return true
                } catch (ignored: NameNotFoundException) {
                }
                return false
            }
        }

        val getAppIntent: Function<String, Intent> =
            Function<String, Intent> { packageName -> packageManager.getLaunchIntentForPackage(packageName) }

        resolverMap = HashMap(8)
        resolverMap["gocatch"] =
            GoCatchBookingResolver(resources, isPackageInstalled, geocoderFactory)
        resolverMap["ingogo"] = IngogoBookingResolver(resources, isPackageInstalled)
        resolverMap["mtaxi"] = MTaxiBookingResolver(isPackageInstalled, getAppIntent)
        resolverMap["uber"] = UberBookingResolver(isPackageInstalled, getAppIntent)
        resolverMap["lyft"] = LyftBookingResolver(resources, isPackageInstalled)
        resolverMap["flitways"] = FlitWaysBookingResolver(geocoderFactory)
        resolverMap["tel:"] = TelBookingResolver(resources)
        resolverMap["sms:"] = SmsBookingResolver()
        resolverMap["http"] = WebBookingResolver(resources)
    }

    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        val externalAction = params.action()
        val resolver = getBookingResolver(externalAction)
        return resolver?.performExternalActionAsync(params)
            ?: Observable.error(
                UnsupportedOperationException(
                    "Strange action: $externalAction"
                )
            )
    }

    override fun getTitleForExternalAction(externalAction: String): String? {
        val resolver = getBookingResolver(externalAction)
        return resolver?.getTitleForExternalAction(externalAction)
    }

    private fun getBookingResolver(externalAction: String?): BookingResolver? {
        return if (externalAction!!.startsWith("lyft")) {
            resolverMap["lyft"]
        } else if (externalAction.startsWith("http")) {
            resolverMap["http"]
        } else if (externalAction.startsWith("tel:")) {
            resolverMap["tel:"]
        } else if (externalAction.startsWith("sms:")) {
            resolverMap["sms:"]
        } else if (resolverMap.containsKey(externalAction)) {
            resolverMap[externalAction]
        } else {
            null
        }
    }
}