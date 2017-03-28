package com.skedgo.android.tripkit

import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import skedgo.tripkit.configuration.ApiKey
import thuytrinh.mockwebserverrule.MockWebServerRule
import java.io.IOException
import java.util.*

class BuiltInInterceptorTest {
  @Rule @JvmField val serverRule = MockWebServerRule()
  private val url: HttpUrl by lazy { serverRule.server.url("/") }

  @Test @Throws(IOException::class, InterruptedException::class)
  fun attachHeaders_full() {
    val interceptor = BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getApiKey { ApiKey("Some id") }
        .userTokenProvider { "Some token" }
        .uuidProvider { "Some UUID" }
        .build()

    serverRule.server.enqueue(MockResponse())
    executeSampleRequest(interceptor)

    val recordedRequest = serverRule.server.takeRequest()
    assertThat(recordedRequest.getHeader("X-TripGo-Version")).isEqualTo("Some version")
    assertThat(recordedRequest.getHeader("Accept-Language")).isEqualTo(Locale.JAPANESE.language)
    assertThat(recordedRequest.getHeader("X-TripGo-Key")).isEqualTo("Some id")
    assertThat(recordedRequest.getHeader("userToken")).isEqualTo("Some token")
    assertThat(recordedRequest.getHeader("X-TripGo-UUID")).isEqualTo("Some UUID")
  }

  @Test @Throws(IOException::class, InterruptedException::class)
  fun attachHeaders_noUuid_withoutProvider() {
    val interceptor = BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getApiKey { ApiKey("Some id") }
        .build()

    serverRule.server.enqueue(MockResponse())
    executeSampleRequest(interceptor)

    val recordedRequest = serverRule.server.takeRequest()
    assertThat(recordedRequest.getHeader("X-TripGo-UUID")).isNull()
  }

  @Test @Throws(IOException::class, InterruptedException::class)
  fun attachHeaders_noUuid_withProvider() {
    val interceptor = BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getApiKey { ApiKey("Some id") }
        .uuidProvider { null }
        .build()

    serverRule.server.enqueue(MockResponse())
    executeSampleRequest(interceptor)

    val recordedRequest = serverRule.server.takeRequest()
    assertThat(recordedRequest.getHeader("X-TripGo-UUID")).isNull()
  }

  @Test(expected = IllegalStateException::class)
  fun appVersionIsMandatory() {
    BuiltInInterceptorBuilder.create()
        .getApiKey { ApiKey("Some id") }
        .locale(Locale.US)
        .build()
  }

  @Test(expected = IllegalStateException::class)
  fun apiKeyIsMandatory() {
    BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.US)
        .build()
  }

  @Test(expected = IllegalStateException::class)
  fun localeIsMandatory() {
    BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .getApiKey { ApiKey("Some id") }
        .build()
  }

  @Test fun userTokenIsOptional() {
    BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getApiKey { ApiKey("Some id") }
        .build()
  }

  @Throws(IOException::class)
  private fun executeSampleRequest(interceptor: Interceptor): Response
      = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()
      .newCall(Request.Builder().url(url).build())
      .execute()
}
