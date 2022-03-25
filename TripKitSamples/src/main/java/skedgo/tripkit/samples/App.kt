package skedgo.tripkit.samples

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import com.skedgo.DaggerTripKit
import com.skedgo.TripKit
import com.skedgo.tripkit.*
import com.skedgo.tripkit.BuildConfig
import com.skedgo.tripkit.configuration.Key
import com.skedgo.tripkit.configuration.Key.ApiKey
import net.danlew.android.joda.JodaTimeAndroid


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        /*
        TripKit.initialize(
            Configs.builder()
                .context(this)
                .key { Key.ApiKey("dfca814e1a213c3e75ac8d4ecffecdb6") }
                .build()
        )
        */

        buildTripKitConfig(this, Key.ApiKey("dfca814e1a213c3e75ac8d4ecffecdb6"))?.let { configs ->

            val httpClientModule = HttpClientModule(null, null, configs)

            val tripKit = DaggerTripKit.builder()
                .mainModule(MainModule(configs))
                .httpClientModule(httpClientModule)
                .build()

            TripKit.initialize(this, tripKit)
        }


    }

    fun buildTripKitConfig(context: Context, key: ApiKey?): Configs? {
        val isDebuggable = 0 != (context.applicationInfo.flags
                and ApplicationInfo.FLAG_DEBUGGABLE) || BuildConfig.DEBUG
        return TripKitConfigs.builder().context(context)
            .debuggable(isDebuggable)
            .userTokenProvider {
                val prefs = context.getSharedPreferences(
                    "UserTokenPreferences",
                    Context.MODE_PRIVATE
                )
                prefs.getString("userToken", "")!!
            }
            .key { key }
            .build()
    }
}
