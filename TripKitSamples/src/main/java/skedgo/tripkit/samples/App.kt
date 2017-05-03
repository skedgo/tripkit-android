package skedgo.tripkit.samples

import android.app.Application
import com.skedgo.android.tripkit.Configs
import skedgo.tripkit.android.TripKit
import net.danlew.android.joda.JodaTimeAndroid
import skedgo.tripkit.configuration.Key

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    JodaTimeAndroid.init(this)
    TripKit.initialize(
        Configs.builder()
            .context(this)
            .key { Key.ApiKey("dfca814e1a213c3e75ac8d4ecffecdb6") }
            .build()
    )
  }
}
