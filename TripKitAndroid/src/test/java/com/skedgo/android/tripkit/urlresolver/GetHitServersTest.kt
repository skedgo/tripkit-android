package com.skedgo.android.tripkit.urlresolver


import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

@Suppress("IllegalIdentifier")
class GetHitServersTest {


  val getBaseServer: GetBaseServer = mock()
  val getLastUsedRegionUrls: GetLastUsedRegionUrls = mock()
  private val getHitServers: GetHitServers by lazy {
    GetHitServers(getBaseServer, getLastUsedRegionUrls)
  }

  @Test
  fun `hit server list should be just base url after error`() {

    val baseUrl = "base url"
    whenever(getBaseServer.execute()).thenReturn(Observable.just(baseUrl))
    whenever(getLastUsedRegionUrls.getLastUsedRegionUrls()).thenReturn(Observable.error(Error()))

    val subscriber = TestSubscriber<Any>()
    getHitServers.execute().subscribe(subscriber)

    subscriber.assertValues(baseUrl)

  }

  @Test
  fun `hit server list should be just base url after null`() {

    val baseUrl = "base url"
    whenever(getBaseServer.execute()).thenReturn(Observable.just(baseUrl))
    whenever(getLastUsedRegionUrls.getLastUsedRegionUrls()).thenReturn(Observable.just(null))

    val subscriber = TestSubscriber<Any>()
    getHitServers.execute().subscribe(subscriber)

    subscriber.assertValues(baseUrl)

  }

  @Test
  fun `hit server list should be url1, url2, base url`() {

    val baseUrl = "base url"
    whenever(getBaseServer.execute()).thenReturn(Observable.just(baseUrl))
    whenever(getLastUsedRegionUrls.getLastUsedRegionUrls()).thenReturn(Observable.just(arrayListOf("url1", "url2")))

    val subscriber = TestSubscriber<Any>()
    getHitServers.execute().subscribe(subscriber)

    subscriber.assertValues("url1", "url2", baseUrl)

  }
}