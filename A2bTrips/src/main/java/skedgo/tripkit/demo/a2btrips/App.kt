package skedgo.tripkit.demo.a2btrips

import android.app.Application
import com.skedgo.android.tripkit.Configs
import com.skedgo.android.tripkit.TripKit
import net.danlew.android.joda.JodaTimeAndroid
import skedgo.tripkit.configuration.ApiKey

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    JodaTimeAndroid.init(this)
    TripKit.initialize(
        Configs.builder()
            .context(this)
            .apiKey { ApiKey("dfca814e1a213c3e75ac8d4ecffecdb6") }
            .build()
    )
  }
}
