package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.util.Gsons
import com.skedgo.tripkit.common.util.Gsons.createForLowercaseEnum
import com.skedgo.tripkit.routing.RoutingResponse
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.reactivex.observers.TestObserver
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Java6Assertions
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.Charset

@RunWith(AndroidJUnit4::class)
class TemporaryUrlApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: TemporaryUrlApi
    private lateinit var baseUrl: HttpUrl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        server = MockWebServer()
        baseUrl = server.url("/")
        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gsons.createForLowercaseEnum()))
            .build()
            .create(TemporaryUrlApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetch trip successfully`() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                IOUtils.toString(
                    this::class.java.getResourceAsStream("/temporaryURL.json"),
                    Charset.defaultCharset()
                )
            )
        server.enqueue(mockResponse)

        val url = baseUrl.newBuilder()
            .addPathSegments("trip/0a1cba21-f177-4706-bbb8-ebd8057e5f4f")
            .build()
        val testObserver: TestObserver<RoutingResponse> = api.requestTemporaryUrlAsync(
            url.toString(),
            emptyMap()
        ).test()

        val response = testObserver.values()[0]
        assertTrue(response.tripGroupList?.isNotEmpty() == true)
    }
}