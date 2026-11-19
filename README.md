# TripKit for Android

The latest verified GitHub release is `v2.2.0` (21 July 2023). `develop`
contains newer, unreleased source; its legacy Maven version is not a new release.
Current source requires minSdk 23, Java 17, Kotlin 2.0.21 and compileSdk 36.

[![Build Status](https://travis-ci.org/skedgo/tripkit-android.svg?branch=master)](https://travis-ci.org/skedgo/tripkit-android)

## Set up TripKit

### Add TripKit to your Android project

First, add [JitPack maven](https://jitpack.io/):

```groovy
allprojects {
  repositories {
    google()
    mavenCentral()
    maven { url "https://jitpack.io" }
  }
}
```

Then, in app's build file, add `TripKitAndroid` dependency into dependencies section:

```groovy
dependencies {
  implementation 'com.github.skedgo.tripkit-android:TripKitAndroid:v2.2.0'
}
```
[![Release](https://jitpack.io/v/skedgo/tripkit-android.svg)](https://jitpack.io/#skedgo/tripkit-android)

For a full setup, you can have a look at TripKitSamples' build file [here](https://github.com/skedgo/tripkit-android/blob/dev/TripKitSamples/build.gradle).

#### Required configuration

##### Supported Android versions

For the current source checkout, set `minSdkVersion 23` and compile with Java 17.
The 2023 release has different requirements; do not treat current source as
already published. Use core library desugaring in Android consumers:

```groovy
android {
  compileSdk 36
  defaultConfig { minSdkVersion 23 }
  compileOptions {
    coreLibraryDesugaringEnabled true
    sourceCompatibility JavaVersion.VERSION_17
    targetCompatibility JavaVersion.VERSION_17
  }
}
dependencies {
  coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.0.4'
}
```

Add `android.enableJetifier=true` on your `gradle.properties` file

```groovy
android.useAndroidX=true
android.enableJetifier=true
android.nonTransitiveRClass=false
```

##### Get an API key

An API key is necessary to use TripKit's services, such as A-2-B routing, and all-day routing. In order to obtain an API key, you can sign up at [https://tripgo.3scale.net](https://tripgo.3scale.net/).

#### Create TripKit instance to access TripKit's services

We recommend to have an `Application` subclass. Next, in the `onCreate()` method, you can initiate following setup:

For the current source checkout:
```kotlin
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    
    val key = Key.ApiKey(yourTripGoApiKey)
    val configs = TripKitConfigs.builder().context(this)
            .debuggable(BuildConfig.DEBUG)
            .key { key }
            .build()

	val httpClientModule = HttpClientModule(null, null, configs)

	val tripKit = DaggerTripKit.builder()
                .mainModule(MainModule(configs))
                .httpClientModule(httpClientModule)
                .build()

    TripKit.initialize(this, tripKit)            
  }
}

```

Read `yourTripGoApiKey` from your own local configuration. Do not commit credentials.

When using TripKitUI, let its matching core dependency resolve transitively instead
of combining it with a different core release. Current `Location` lives in
`com.skedgo.tripkit.common.model.location` and its `id` is a `String`.
A future release requires consumer, R8 and runtime validation; a build alone is
not a compatibility signoff.
