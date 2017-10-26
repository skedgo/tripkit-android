# TripKit for Android
[![Build Status](https://travis-ci.org/skedgo/tripkit-android.svg?branch=dev)](https://travis-ci.org/skedgo/tripkit-android)

## Set up TripKit

### Add TripKit to your Android project

For now we recommend including [TripKit](https://github.com/skedgo/tripkit-android) as a [git submodule](https://git-scm.com/docs/git-submodule) in your project. Once done, you can add the [TripKitAndroid](https://github.com/skedgo/tripkit-android/tree/dev/TripKitAndroid) module as a dependency to your `build.gradle` file:

```groovy
// In app's build file.
dependencies {
  compile project(':TripKitAndroid')
}
```

For a full setup, you can have a look at TripKitSamples' build file [here](https://github.com/skedgo/tripkit-android/blob/dev/TripKitSamples/build.gradle).

#### Required configuration

##### Supported Android versions

TripKit supports for Android apps running [Android 4.0.3](https://developer.android.com/about/versions/android-4.0.3.html) and above. To make sure that it works in your Android app, please specify `minSdkVersion` in your `build.gradle` file to `15`:

```groovy
android {
  defaultConfig {
    minSdkVersion 15
  }
}
```

##### Get an API key

An API key is necessary to use TripKit's services, such as A-2-B routing, and all-day routing. In order to obtain an API key, you can sign up at [https://tripgo.3scale.net](https://tripgo.3scale.net/).

#### Create TripKit instance to access TripKit's services

We recommend to have an `Application` subclass. Next, in the `onCreate()` method, you can initiate following setup:

```kotlin
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    JodaTimeAndroid.init(this)
    TripKit.initialize(
        Configs.builder()
            .context(this)
            .key { Key.ApiKey("YOUR_API_KEY") }
            .build()
    )
  }
}

```

With `"YOUR_API_KEY"` is the key that you obtained from [https://tripgo.3scale.net](https://tripgo.3scale.net) in the previous step.
