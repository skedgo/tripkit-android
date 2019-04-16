package com.skedgo.android.tripkit;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

public class TestRunner extends RobolectricTestRunner {
  public TestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }
}