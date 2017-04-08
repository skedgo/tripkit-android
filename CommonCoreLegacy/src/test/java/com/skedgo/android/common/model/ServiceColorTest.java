package com.skedgo.android.common.model;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class ServiceColorTest {
  @Test
  public void shouldEqual() {
    assertThat(new ServiceColor(1, 2, 3)).isEqualTo(new ServiceColor(1, 2, 3));
  }

  @Test
  public void shouldNotEqual() {
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo(new ServiceColor(0, 2, 3));
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo(new ServiceColor(1, 0, 3));
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo(new ServiceColor(1, 2, 0));
    assertThat(new ServiceColor(1, 2, 3)).isNotEqualTo("Awesome!");
  }
}