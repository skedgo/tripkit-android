package skedgo.tripkit.samples

import android.app.Application
import com.skedgo.TripKit
import com.skedgo.tripkit.Configs
import net.danlew.android.joda.JodaTimeAndroid
import com.skedgo.tripkit.configuration.Key

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
