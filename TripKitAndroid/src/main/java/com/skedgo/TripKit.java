package com.skedgo;

import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import com.skedgo.tripkit.Configs;
import com.skedgo.tripkit.HttpClientModule;
import com.skedgo.tripkit.LocationInfoService;
import com.skedgo.tripkit.MainModule;
import com.skedgo.tripkit.data.TripKitPreferencesModule;
import com.skedgo.tripkit.data.regions.RegionService;
import com.skedgo.tripkit.TripUpdater;
import com.skedgo.tripkit.bookingproviders.BookingResolver;

import io.reactivex.functions.Consumer;

import com.skedgo.tripkit.a2brouting.A2bRoutingDataModule;
import com.skedgo.tripkit.notification.NotificationKt;
import com.skedgo.tripkit.routing.GeoLocation;
import com.skedgo.tripkit.routing.GetOffAlertCache;
import com.skedgo.tripkit.tsp.TspModule;

import javax.inject.Singleton;

import dagger.Component;

import com.skedgo.tripkit.a2brouting.RouteService;
import com.skedgo.tripkit.android.A2bRoutingComponent;
import com.skedgo.tripkit.android.AnalyticsComponent;
import com.skedgo.tripkit.android.DateTimeComponent;
import com.skedgo.tripkit.android.FetchRegionsService;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import static com.skedgo.tripkit.routing.TripAlarmBroadcastReceiver.NOTIFICATION_CHANNEL_START_TRIP;
import static com.skedgo.tripkit.routing.TripAlarmBroadcastReceiver.NOTIFICATION_CHANNEL_START_TRIP_ID;

@Singleton
@Component(modules = {
        HttpClientModule.class,
        A2bRoutingDataModule.class,
        TspModule.class,
        MainModule.class,
        TripKitPreferencesModule.class
})
public abstract class TripKit {
    private static TripKit instance;

    public static TripKit getInstance() {
        synchronized (TripKit.class) {
            if (instance == null) {
                throw new IllegalStateException("Must initialize TripKit before using getInstance()");
            }

            return instance;
        }
    }

    /**
     * This gives a chance to provide a custom {@link TripKit}.
     * One idea is that we can create {@link DaggerTripKit}
     * w/ some customized modules.
     * <p>
     * Note that you should only use this
     * when you totally understand what you're doing.
     * Otherwise, just go with {@link #initialize(Configs)} instead.
     *
     * @param context A {@link Context} to launch {@link FetchRegionsService}.
     * @param tripKit Can be created via {@link DaggerTripKit}.
     */
    public static void initialize(@NonNull Context context, @NonNull TripKit tripKit) {
        synchronized (TripKit.class) {
            if (instance == null) {
                instance = tripKit;
            }
            FetchRegionsService.Companion.scheduleAsync(context)
                    .subscribe(unused -> {
                    }, instance.getErrorHandler());
        }
    }

    public static boolean isInitialized() {
        return (instance != null);
    }

    public static void initialize(Configs configs) {
        synchronized (TripKit.class) {
            if (configs == null) {
                throw new IllegalStateException("Must initialize Configs before using initialize()");
            }

            if (instance == null) {
                instance = DaggerTripKit.builder()
                        .mainModule(new MainModule(configs))
                        .httpClientModule(new HttpClientModule(
                                null,
                                null,
                                configs,
                                null,
                                null
                        ))
                        .build();
                JodaTimeAndroid.init(configs.context());
                GetOffAlertCache.INSTANCE.init(configs.context());
                GeoLocation.INSTANCE.init(configs.context());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    List<NotificationChannel> channels = new ArrayList<>();
                    channels.add(NotificationKt.createChannel(
                            NOTIFICATION_CHANNEL_START_TRIP_ID,
                            NOTIFICATION_CHANNEL_START_TRIP)
                    );
                    NotificationKt.createNotificationChannels(
                            configs.context(),
                            channels
                    );
                }
            }

            FetchRegionsService.Companion.scheduleAsync(configs.context())
                    .subscribe(unused -> {
                    }, instance.getErrorHandler());
        }
    }

    public abstract Configs configs();

    public abstract RegionService getRegionService();

    public abstract RouteService getRouteService();

    public abstract okhttp3.OkHttpClient getOkHttpClient3();

    public abstract BookingResolver getBookingResolver();

    public abstract LocationInfoService getLocationInfoService();

    public abstract TripUpdater getTripUpdater();

    public abstract A2bRoutingComponent a2bRoutingComponent();

    public abstract AnalyticsComponent analyticsComponent();

    public abstract DateTimeComponent dateTimeComponent();

    public abstract Consumer<Throwable> getErrorHandler();
}
