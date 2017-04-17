package com.skedgo.android.tripkit.urlresolver

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.BuildConfig
import com.skedgo.android.tripkit.TestRunner
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.robolectric.annotation.Config
import rx.Observable
import java.io.IOException

@Suppress("IllegalIdentifier")
@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class UrlResolverInterceptorTest {

  val getHitServers: GetHitServers = mock()
  val getBaseServer: GetBaseServer = mock()
  private val urlResolverInterceptor: UrlResolverInterceptor by lazy {
    UrlResolverInterceptor(getHitServers, getBaseServer)
  }

  @Test @Throws(IOException::class)
  fun `should not override url`() {

    val notBaseTripGoUrl = "https://not.tripgo.skedgo.com/satapp"

    whenever(getBaseServer.execute())
        .thenReturn(Observable.just("https://tripgo.skedgo.com/satapp"))

    val chain = mock<Interceptor.Chain>()
    val chainRequest = Request.Builder()
        .url(notBaseTripGoUrl + "/service.json")
        .build()
    whenever(chain.request()).thenReturn(chainRequest)

    urlResolverInterceptor.intercept(chain)

    verify(chain).proceed(ArgumentMatchers.same(chainRequest))
  }

  //@Test NPE in response.isSuccessful, Response cannot be mock
  @Throws(IOException::class)
  fun `should override url`() {

    val tripGoBaseUrl = "https://tripgo.skedgo.com/satapp"
    val tripGoUrl1 = "https://granduni.skedgo.com/satapp"

    whenever(getBaseServer.execute())
        .thenReturn(Observable.just(tripGoBaseUrl))

    whenever(getHitServers.execute())
        .thenReturn(Observable.just(tripGoUrl1))

    val chain = mock<Interceptor.Chain>()
    val chainRequest = Request.Builder()
        .url(tripGoBaseUrl + "/service.json")
        .build()
    whenever(chain.request()).thenReturn(chainRequest)

    val expectedRequest = chainRequest.newBuilder()
        .url(HttpUrl.parse(tripGoUrl1 +"/service.json"))
        .build()

    urlResolverInterceptor.intercept(chain)

    verify(chain).proceed(ArgumentMatchers.argThat {
      request -> request.url() != expectedRequest.url()})
  }

}