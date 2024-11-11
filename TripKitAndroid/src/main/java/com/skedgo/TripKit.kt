package com.skedgo

import android.app.NotificationChannel
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.skedgo.tripkit.Configs
import com.skedgo.tripkit.HttpClientModule
import com.skedgo.tripkit.LocationInfoService
import com.skedgo.tripkit.MainModule
import com.skedgo.tripkit.TripUpdater
import com.skedgo.tripkit.a2brouting.A2bRoutingDataModule
import com.skedgo.tripkit.a2brouting.RouteService
import com.skedgo.tripkit.android.A2bRoutingComponent
import com.skedgo.tripkit.android.AnalyticsComponent
import com.skedgo.tripkit.android.DateTimeComponent
import com.skedgo.tripkit.android.FetchRegionsService
import com.skedgo.tripkit.android.FetchRegionsService.Companion.scheduleAsync
import com.skedgo.tripkit.bookingproviders.BookingResolver
import com.skedgo.tripkit.data.TripKitPreferencesModule
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.notification.createChannel
import com.skedgo.tripkit.notification.createNotificationChannels
import com.skedgo.tripkit.routing.GeoLocation
import com.skedgo.tripkit.routing.GetOffAlertCache
import com.skedgo.tripkit.routing.TripAlarmBroadcastReceiver
import com.skedgo.tripkit.tsp.TspModule
import dagger.Component
import io.reactivex.functions.Consumer
import net.danlew.android.joda.JodaTimeAndroid
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        HttpClientModule::class,
        A2bRoutingDataModule::class,
        TspModule::class,
        MainModule::class,
        TripKitPreferencesModule::class
    ]
)
abstract class TripKit {
    abstract fun configs(): Configs

    abstract val regionService: RegionService

    abstract val routeService: RouteService

    abstract val okHttpClient3: OkHttpClient

    abstract val bookingResolver: BookingResolver

    abstract val locationInfoService: LocationInfoService

    abstract val tripUpdater: TripUpdater

    abstract fun a2bRoutingComponent(): A2bRoutingComponent

    abstract fun analyticsComponent(): AnalyticsComponent

    abstract fun dateTimeComponent(): DateTimeComponent

    abstract val errorHandler: Consumer<Throwable>

    companion object {
        private var instance: TripKit? = null

        fun getInstance(): TripKit {
            synchronized(TripKit::class.java) {
                checkNotNull(instance) { "Must initialize TripKit before using getInstance()" }
                return instance!!
            }
        }

        /**
         * This gives a chance to provide a custom [TripKit].
         * One idea is that we can create [DaggerTripKit]
         * w/ some customized modules.
         *
         *
         * Note that you should only use this
         * when you totally understand what you're doing.
         * Otherwise, just go with [.initialize] instead.
         *
         * @param context A [Context] to launch [FetchRegionsService].
         * @param tripKit Can be created via [DaggerTripKit].
         */
        @JvmStatic
        fun initialize(context: Context, tripKit: TripKit) {
            synchronized(TripKit::class.java) {
                if (instance == null) {
                    instance = tripKit
                }
                scheduleAsync(context)
                    .subscribe({ unused: Void? -> }, instance!!.errorHandler)
            }
        }

        @JvmStatic
        val isInitialized: Boolean
            get() = (instance != null)

        @JvmStatic
        fun initialize(configs: Configs) {
            synchronized(TripKit::class.java) {
                if (instance == null) {
                    instance = DaggerTripKit.builder()
                        .mainModule(MainModule(configs))
                        .httpClientModule(
                            HttpClientModule(
                                null,
                                null,
                                configs,
                                null,
                                null
                            )
                        )
                        .build()
                    JodaTimeAndroid.init(configs.context())
                    GetOffAlertCache.init(configs.context())
                    GeoLocation.init(configs.context())
                    if (VERSION.SDK_INT >= VERSION_CODES.O) {
                        val channels: MutableList<NotificationChannel> = ArrayList()
                        channels.add(
                            createChannel(
                                TripAlarmBroadcastReceiver.NOTIFICATION_CHANNEL_START_TRIP_ID,
                                TripAlarmBroadcastReceiver.NOTIFICATION_CHANNEL_START_TRIP
                            )
                        )
                        configs.context()
                            .createNotificationChannels(
                                channels
                            )
                    }
                }
                scheduleAsync(configs.context())
                    .subscribe({ unused: Void -> }, instance?.errorHandler)
            }
        }
    }
}
