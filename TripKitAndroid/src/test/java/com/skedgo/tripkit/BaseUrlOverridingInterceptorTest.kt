package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Callable

@RunWith(AndroidJUnit4::class)
class BaseUrlOverridingInterceptorTest {

    private lateinit var baseUrlAdapter: Callable<String>
    private lateinit var interceptor: BaseUrlOverridingInterceptor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        baseUrlAdapter = mockk()
        interceptor = BaseUrlOverridingInterceptor(baseUrlAdapter)
    }

    @Test
    fun `override Satapp request without query params`() {
        every { baseUrlAdapter.call() } returns "https://granduni.buzzhives.com/satapp-beta/"

        val chain = mockk<Interceptor.Chain>(relaxed = true)
        val chainRequest = Request.Builder()
            .url("https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp/regions.json")
            .build()
        every { chain.request() } returns chainRequest

        val expectedRequest = chainRequest.newBuilder()
            .url("https://granduni.buzzhives.com/satapp-beta/regions.json")
            .build()

        interceptor.intercept(chain)

        verify { chain.proceed(withArg { request ->
            assert(request.url == expectedRequest.url)
            assert(request.method == expectedRequest.method)
        })}
    }

    @Test
    fun `override Satapp request with query params`() {
        every { baseUrlAdapter.call() } returns "https://granduni.buzzhives.com/satapp-beta/"

        val chain = mockk<Interceptor.Chain>(relaxed = true)
        val chainRequest = Request.Builder()
            .url("https://lepton-us-co-denver.tripgo.skedgo.com/satapp/routing.json?modes=ps_tax&v=11&arriveBefore=0&tt=0&departAfter=1459485056&version=a-beta4.5.1-debug".toHttpUrl())
            .build()
        every { chain.request() } returns chainRequest

        val expectedRequest = chainRequest.newBuilder()
            .url("https://granduni.buzzhives.com/satapp-beta/routing.json?modes=ps_tax&v=11&arriveBefore=0&tt=0&departAfter=1459485056&version=a-beta4.5.1-debug".toHttpUrl())
            .build()

        interceptor.intercept(chain)

        verify { chain.proceed(withArg { request ->
            assert(request.url == expectedRequest.url)
            assert(request.method == expectedRequest.method)
        })}
    }

    @Test
    fun `ignore non Tripgo request`() {
        every { baseUrlAdapter.call() } returns "https://granduni.buzzhives.com/satapp-beta/"

        val chain = mockk<Interceptor.Chain>(relaxed = true)
        val chainRequest = Request.Builder()
            .url("https://google.com/haha".toHttpUrl())
            .build()
        every { chain.request() } returns chainRequest

        interceptor.intercept(chain)

        verify { chain.proceed(chainRequest) }
    }

    @Test
    fun `ignore if no new base URL available`() {
        every { baseUrlAdapter.call() } returns null

        val chain = mockk<Interceptor.Chain>(relaxed = true)
        val chainRequest = Request.Builder()
            .url("https://skedgo.com/tripgo".toHttpUrl())
            .build()
        every { chain.request() } returns chainRequest

        interceptor.intercept(chain)

        verify { chain.proceed(chainRequest) }
    }
}