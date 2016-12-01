package com.skedgo.android.common.util;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.R;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StringGenerationToolTest {

  @Test
  public void spacePrefixPostfixTest() {
    assertThat(RuntimeEnvironment.application.getResources().getString(R.string._at_))
        .startsWith(" ")
        .endsWith(" ");
  }
}
