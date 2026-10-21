package com.skedgo.tripkit

import android.content.res.Resources
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class StrictTripSubscriptionTest {
    @Test fun `invalid absolute subscription URLs never reach the HTTP API`() {
        val api = mockk<TripUpdateApi>()
        every { api.tripSubscription(any()) } returns Observable.just(Response.success<Void>(null))
        val updater = TripUpdaterImpl(mockk<Resources>(), api, Gson())
        for (url in listOf("", "   ", "null", "not-a-url", "/subscribe/123", "//valid.example/path",
            "javascript:alert(1)", "https://", "https:valid.example/path", "file:///tmp/subscribe",
            "content://trip/123", "ftp://valid.example/path", "https://bad host/path", "https://valid.example:99999/path")) {
            val observer = updater.tripSubscription(url).test()
            observer.awaitTerminalEvent()
            observer.assertError(IllegalArgumentException::class.java).assertNoValues()
        }
        verify(exactly = 0) { api.tripSubscription(any()) }
    }
    @Test fun `supported HTTP URLs use client canonicalization without losing query or port`() {
        val api = mockk<TripUpdateApi>()
        every { api.tripSubscription(any()) } returns Observable.just(Response.success<Void>(null))
        val updater = TripUpdaterImpl(mockk<Resources>(), api, Gson())
        for (url in listOf("https://valid.example/path", "http://valid.example/path",
            "HTTPS://valid.example:8443/a%20b?key=x%2Fy&v=1")) {
            val observer = updater.tripSubscription("  $url  ").test()
            observer.awaitTerminalEvent(); observer.assertNoErrors().assertValueCount(1)
        }
        verify { api.tripSubscription("https://valid.example:8443/a%20b?key=x%2Fy&v=1") }
        verify { api.tripSubscription("http://valid.example/path") }
    }
}
