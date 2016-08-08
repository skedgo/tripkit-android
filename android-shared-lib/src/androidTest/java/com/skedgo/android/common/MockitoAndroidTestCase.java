package com.skedgo.android.common;

import android.test.AndroidTestCase;

public class MockitoAndroidTestCase extends AndroidTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
  }
}