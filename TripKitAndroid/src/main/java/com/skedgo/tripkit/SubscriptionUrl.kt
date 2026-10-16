package com.skedgo.tripkit

import com.skedgo.tripkit.routing.Trip
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/** Absolute HTTP(S) subscription endpoints, parsed and canonicalized by the HTTP client. */
fun validatedSubscriptionUrl(value: String?): String? {
    val candidate = value?.trim() ?: return null
    // Retrofit @Url also resolves relative references; trip capabilities must be absolute.
    if (!candidate.startsWith("https://", ignoreCase = true) &&
        !candidate.startsWith("http://", ignoreCase = true)) return null
    return candidate.toHttpUrlOrNull()?.toString()
}

fun hasValidSubscribeUrl(trip: Trip): Boolean = validatedSubscriptionUrl(trip.subscribeURL) != null
