package com.skedgo.android.tripkit.booking;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

public class TestRunner extends RobolectricTestRunner {
  public TestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override protected AndroidManifest getAppManifest(Config config) {
    // After upgrading to gradle plugin v2.2.0, the file path of
    // the AM file no longer contains `full` but `aapt`.
    // As a result, the RobolectricTestRunner complained
    // that it didn't find the AM file, resulting in a bunch of failures.
    // So this patch was born to fix that issue. Note that it might be
    // incompatible w/ future releases of Robolectric.
    final AndroidManifest appManifest = super.getAppManifest(config);
    final FsFile androidManifestFile = appManifest.getAndroidManifestFile();
    final String androidManifestFilePath = androidManifestFile.getPath().replace("full", "aapt");

    return new AndroidManifest(
        FileFsFile.from(androidManifestFilePath),
        appManifest.getResDirectory(),
        appManifest.getAssetsDirectory()
    );
  }
}
