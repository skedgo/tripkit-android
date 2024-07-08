package com.skedgo.tripkit.extensions

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun String.buildUrlWithQueryParams(
    modes: List<String>,
    excludedTransitModes: List<String>,
    excludeStops: List<String>,
    options: Map<String, Any>
): String {
    val urlBuilder = this.toHttpUrlOrNull()?.newBuilder()
        ?.addPathSegment("routing.json")

    modes.forEach { mode ->
        urlBuilder?.addQueryParameter("modes", mode)
    }

    excludedTransitModes.forEach { excludedMode ->
        urlBuilder?.addQueryParameter("avoid", excludedMode)
    }

    excludeStops.forEach { excludeStop ->
        urlBuilder?.addQueryParameter("avoidStops", excludeStop)
    }

    options.forEach { (key, value) ->
        urlBuilder?.addQueryParameter(key, value.toString())
    }

    return urlBuilder?.build().toString()
}

