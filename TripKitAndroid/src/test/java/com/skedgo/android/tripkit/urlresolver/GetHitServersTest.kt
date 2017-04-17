package com.skedgo.android.tripkit.urlresolver


import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.common.model.Region
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

@Suppress("IllegalIdentifier")
class GetHitServersTest {


  val getBaseServer: GetBaseServer = mock()
  val getLastUsedRegion: GetLastUsedRegion = mock()
  private val getHitServers: GetHitServers by lazy {
    GetHitServers(getBaseServer, getLastUsedRegion)
  }

  @Test fun `hit server list should be just base url`() {

    val baseUrl = "base url"
    whenever(getBaseServer.execute()).thenReturn(Observable.just(baseUrl))
    whenever(getLastUsedRegion.execute()).thenReturn(Observable.error(Error()))

    val subscriber = TestSubscriber<Any>()
    getHitServers.execute().subscribe(subscriber)

    subscriber.assertValues(baseUrl)

  }

  @Test fun `hit server list should be url1, url2, base url`() {

    val region: Region = mock()
    whenever(region.urLs).thenReturn(arrayListOf("url1", "url2"))

    val baseUrl = "base url"
    whenever(getBaseServer.execute()).thenReturn(Observable.just(baseUrl))
    whenever(getLastUsedRegion.execute()).thenReturn(Observable.just(region))

    val subscriber = TestSubscriber<Any>()
    getHitServers.execute().subscribe(subscriber)

    subscriber.assertValues("url1", "url2", baseUrl)

  }
}