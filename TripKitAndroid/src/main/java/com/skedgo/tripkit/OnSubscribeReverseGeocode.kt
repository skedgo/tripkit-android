package com.skedgo.tripkit

import android.content.Context
import android.location.Address
import android.location.Geocoder
import io.reactivex.*

internal class OnSubscribeReverseGeocode(
    private val context: Context,
    private val latitude: Double,
    private val longitude: Double,
    private val maxResults: Int
) : ObservableOnSubscribe<List<Address>> {
    override fun subscribe(emitter: ObservableEmitter<List<Address>>) {
        if (!Geocoder.isPresent()) {
            emitter.onError(UnsupportedOperationException("Geocoder not present"))
            return
        }
        val geocoder = Geocoder(context)
        try {
            emitter.onNext(geocoder.getFromLocation(latitude, longitude, maxResults))
            emitter.onComplete()
        } catch (e: Exception) {
            if (!emitter.isDisposed) {
                emitter.onError(e)
            }
        }
    }
}