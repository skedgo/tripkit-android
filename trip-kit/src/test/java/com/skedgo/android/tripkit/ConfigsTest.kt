package com.skedgo.android.tripkit

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.robolectric.RuntimeEnvironment
import skedgo.tripkit.configuration.ApiKey

class ConfigsTest {
  @Test fun uuidOptedOutShouldBeFalseByDefault() {
    val configs = Configs.builder()
        .apiKey { ApiKey("Doesn't look like anything to me") }
        .context(mock())
        .build()
    assertThat(configs.isUuidOptedOut()).isFalse()
  }

  @Test fun shouldNotBeDebuggableByDefault() {
    val configs = Configs.builder()
        .apiKey { ApiKey("Doesn't look like anything to me") }
        .context(mock())
        .build()
    assertThat(configs.debuggable()).isFalse()
  }

  @Test(expected = NullPointerException::class)
  fun regionEligibilityShouldBeNonNull() {
    Configs.builder()
        .apiKey(null)
        .context(RuntimeEnvironment.application)
        .build()
  }

  @Test(expected = NullPointerException::class)
  fun contextShouldBeNonNull() {
    Configs.builder()
        .apiKey { ApiKey("Doesn't look like anything to me") }
        .context(null)
        .build()
  }
}
