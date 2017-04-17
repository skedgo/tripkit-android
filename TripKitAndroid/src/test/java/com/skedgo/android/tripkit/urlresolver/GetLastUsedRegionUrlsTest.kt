package com.skedgo.android.tripkit.urlresolver

import android.content.Context
import android.support.v4.util.ArraySet
import com.skedgo.android.tripkit.BuildConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.observers.TestSubscriber
import org.assertj.core.api.Assertions.assertThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.common.model.Region

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
@Suppress("IllegalIdentifier")
class GetLastUsedRegionUrlsTest {

  val prefs = RuntimeEnvironment.application.getSharedPreferences("", Context.MODE_PRIVATE)
  internal val getLastUsedRegionUrls: GetLastUsedRegionUrls by lazy {
    GetLastUsedRegionUrls(prefs)
  }

  @Test
  fun `should have saved urls`() {

    val regionUrls = listOf("url 1", "url 2")

    prefs.edit().putStringSet(LAST_REGION_URLS, ArraySet(regionUrls)).apply()

    val subscriber = TestSubscriber<List<String>>()
    getLastUsedRegionUrls.getLastUsedRegionUrls()
        .subscribe(subscriber)

    val urls: List<String> = subscriber.onNextEvents[0]

    assertThat(urls).containsAll(regionUrls)
  }

  @Test
  fun `should have no saved urls`() {

    val subscriber = TestSubscriber<List<String>>()
    getLastUsedRegionUrls.getLastUsedRegionUrls()
        .subscribe(subscriber)

    val urls: List<String>? = subscriber.onNextEvents[0]

    assertThat(urls).isNull()

  }

  @Test
  fun `should have saved region name`() {

    prefs.edit().putString(LAST_REGION_NAME, "region").apply()

    val region: Region = mock()
    whenever(region.name).thenReturn("region")

    assertThat(getLastUsedRegionUrls.isSavedRegion(region)).isTrue()

  }

  @Test
  fun `should have not saved region name`() {

    val region: Region = mock()
    whenever(region.name).thenReturn("region")

    assertThat(getLastUsedRegionUrls.isSavedRegion(region)).isFalse()

  }

  @Test
  fun `should have not saved region with different name`() {

    prefs.edit().putString(LAST_REGION_NAME, "other region").apply()

    val region: Region = mock()
    whenever(region.name).thenReturn("region")

    assertThat(getLastUsedRegionUrls.isSavedRegion(region)).isFalse()

  }

}