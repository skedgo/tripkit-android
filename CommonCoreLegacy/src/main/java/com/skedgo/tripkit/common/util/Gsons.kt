package com.skedgo.tripkit.common.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skedgo.tripkit.common.model.booking.GsonAdaptersBooking
import com.skedgo.tripkit.common.model.realtimealert.GsonAdaptersRealtimeAlert
import com.skedgo.tripkit.common.model.region.Region.City

object Gsons {
    @JvmStatic
    fun createForLowercaseEnum(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(LowercaseEnumTypeAdapterFactory())
            .registerTypeAdapterFactory(GsonAdaptersBooking())
            .registerTypeAdapterFactory(GsonAdaptersRealtimeAlert())
            .create()
    }

    @JvmStatic
    fun createForRegion(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(City::class.java, CityJsonDeserializer())
            .create()
    }
}