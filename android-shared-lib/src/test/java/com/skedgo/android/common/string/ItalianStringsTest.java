package com.skedgo.android.common.string;

import android.content.res.Resources;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.R;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, qualifiers = "it")
public class ItalianStringsTest {
  private final Resources resources = RuntimeEnvironment.application.getResources();

  @Test public void someStringsHaveCorrectValues() {
    assertThat(resources.getString(R.string.approximately_s_away))
        .isEqualTo("Circa %s di distanza");
  }
}