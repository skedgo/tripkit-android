package com.skedgo.android.tripkit.booking.ui

import org.junit.runners.model.InitializationError
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.manifest.AndroidManifest
import org.robolectric.res.FileFsFile


class TestRunner @Throws(InitializationError::class)
constructor(klass: Class<*>) : RobolectricTestRunner(klass) {

  override fun getAppManifest(config: Config): AndroidManifest {
    // After upgrading to gradle plugin v2.2.0, the file path of
    // the AM file no longer contained `full` but `aapt`.
    // As a result, the RobolectricTestRunner complained
    // that it didn't find the AM file, resulting a bunch of failures.
    // So this patch was born to fix that issue. Note that it might be
    // incompatible w/ future release of Robolectric.
    val appManifest = super.getAppManifest(config)
    val androidManifestFile = appManifest.androidManifestFile
    val androidManifestFilePath = androidManifestFile.path.replace("full", "aapt")

    return AndroidManifest(
        FileFsFile.from(androidManifestFilePath),
        appManifest.resDirectory,
        appManifest.assetsDirectory
    )
  }
}