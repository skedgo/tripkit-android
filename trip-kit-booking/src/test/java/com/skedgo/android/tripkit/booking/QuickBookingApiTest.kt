package com.skedgo.android.tripkit.booking

import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.observers.TestSubscriber
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

@Suppress("IllegalIdentifier")
class QuickBookingApiTest {

  private var server = MockWebServer()
  private var baseUrl = server.url("/")
  private val api: QuickBookingApi by lazy {
    val gson = GsonBuilder()
        .registerTypeAdapterFactory(GsonAdaptersQuickBooking())
        .create()
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create<QuickBookingApi>(QuickBookingApi::class.java)
  }

  @After
  @Throws(IOException::class)
  fun after() {
    server.shutdown()
  }

  @Test
  fun `should get quick booking list`() {

    val mockResponse = MockResponse()
    mockResponse.setResponseCode(200)
    mockResponse.setBody(IOUtils.toString(
        javaClass.getResourceAsStream("/quick-booking-response.json"),
        Charset.defaultCharset()
    ))
    server.enqueue(mockResponse)

    val url = baseUrl.newBuilder()
        .addPathSegment("quick")
        .addPathSegment("booking")
        .addPathSegment("response")
        .build()
        .toString()

    val subscriber = TestSubscriber<List<QuickBooking>>()
    api.fetchQuickBookingsAsync(url).subscribe(subscriber)

    val quickBooking = ImmutableQuickBooking
        .builder()
        .bookingURL("https://<satapp_server>/booking/v1/c21199c6-0165-40ec-9de4-fee43a0eb521/-15730924761/quick/0eec2f54-8cab-45ba-852a-8ddeb7a65a1e")
        .tripUpdateURL("https://<satapp_server>/booking/v1/c21199c6-0165-40ec-9de4-fee43a0eb521/-15730924761/update/0eec2f54-8cab-45ba-852a-8ddeb7a65a1e")
        .priceInUSD(23.512814f)
        .imageURL("http://d1a3f4spazzrp4.cloudfront.net/car-types/mono/mono-black.png")
        .title("UberBLACK")
        .subtitle("Sandbox: Always surge pricing")
        .bookingTitle("Request")
        .priceString("\$27-30")
        .price(30f)
        .eta(360f)
        .build()

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertValue(Arrays.asList<QuickBooking>(quickBooking))
  }

}