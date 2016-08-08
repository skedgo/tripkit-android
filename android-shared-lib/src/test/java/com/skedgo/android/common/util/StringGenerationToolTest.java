package com.skedgo.android.common.util;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StringGenerationToolTest {

  @Test
  public void spacePrefixPostfixTest() {
    assertThat(RuntimeEnvironment.application.getResources().getString(R.string._at_))
        .startsWith(" ")
        .endsWith(" ");
  }
}
