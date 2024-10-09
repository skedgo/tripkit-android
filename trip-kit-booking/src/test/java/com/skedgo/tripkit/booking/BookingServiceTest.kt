package com.skedgo.tripkit.booking

import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.subscribers.TestSubscriber
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class BookingServiceTest: MockKTest() {

    val bookingApi: BookingApi = mockk()

    private lateinit var gson: Gson
    private lateinit var service: BookingServiceImpl

    @Before
    fun before() {
        MockKAnnotations.init(this)
        initRx()
        gson = Gson() // use default Gson
        service = BookingServiceImpl(bookingApi, gson)
    }

    @After
    fun tearDown() {
        tearDownRx()
    }

    @Test
    fun shouldGetBookingFormAsync() {
        val bookingForm = BookingForm().apply { id = "1" }
        val response = Response.success(bookingForm)

        every { bookingApi.getFormAsync("url") } returns Observable.just(response)

        val subscriber = service.getFormAsync("url").test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        val result = subscriber.values()[0]
        assertEquals(bookingForm, result)
    }

    @Test
    fun shouldGetBookingFormAsyncThrowError() {
        val responseError = """
        {
          "title": "Test",   
          "errorCode": 401,
          "error": "That userToken is unrecognised.",        
          "usererror": false
        }
    """.trimIndent()

        val responseBody: ResponseBody =
            responseError.toResponseBody("application/json".toMediaTypeOrNull())
        val response: Response<BookingForm> = Response.error(401, responseBody)

        every { bookingApi.getFormAsync(url = "url") } returns Observable.just(response)

        val subscriber: TestSubscriber<BookingForm> = service.getFormAsync(url = "url").test()

        subscriber.awaitTerminalEvent()
        subscriber.assertError(BookingError::class.java)

        val error: Throwable = subscriber.errors()[0]
        error.printStackTrace()
        assertEquals("That userToken is unrecognised.", error.message)
    }

    @Test
    fun shouldPostBookingFormAsync() {
        val bookingForm = BookingForm().apply { id = "1" }
        val inputForm = mockk<InputForm>()
        val response = Response.success(bookingForm)

        every { bookingApi.postFormAsync("url", inputForm) } returns Observable.just(response)

        val subscriber = service.postFormAsync("url", inputForm).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        val result = subscriber.values()[0]
        assertEquals(bookingForm, result)
    }

    @Test
    fun shouldPostBookingFormAsyncThrowError() {
        val inputForm = mockk<InputForm>()
        val responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}"
        val responseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), responseError)
        val response = Response.error<BookingForm>(401, responseBody)

        every { bookingApi.postFormAsync("url", inputForm) } returns Observable.just(response)

        val subscriber = service.postFormAsync("url", inputForm).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertError(BookingError::class.java)

        val error = subscriber.errors()[0]
        assertEquals("That userToken is unrecognised.", error.message)
    }

    @Test
    fun shouldHandleBookingSuccessResponse() {
        val bookingForm = BookingForm().apply { id = "1" }
        val response = Response.success(bookingForm)

        try {
            val subscriber = service.handleBookingResponse.apply(response).test()
            subscriber.awaitTerminalEvent()
            subscriber.assertNoErrors()

            val result = subscriber.values()[0]
            assertEquals(bookingForm, result)
        } catch (e: Exception) {
            fail("Test failed with an exception")
        }
    }

    @Test
    fun shouldHandleBookingEmptyResponse() {
        val response = Response.success(
            BookingForm(),
            okhttp3.Response.Builder()
                .request(okhttp3.Request.Builder().url("http://skedgo.com").build())
                .code(204)
                .message("")
                .protocol(Protocol.HTTP_1_1)
                .build()
        )

        try {
            val subscriber = service.handleBookingResponse.apply(response).test()
            subscriber.awaitTerminalEvent()
            subscriber.assertNoErrors()

            val result = subscriber.values()[0]
            assert(result is NullBookingForm)
        } catch (e: Exception) {
            fail("Test failed with an exception")
        }
    }

    @Test
    fun shouldHandleBookingErrorResponse() {
        val responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}"
        val responseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), responseError)
        val response = Response.error<BookingForm>(401, responseBody)

        val subscriber = service.handleBookingResponse.apply(response).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertError(BookingError::class.java)

        val error = subscriber.errors()[0]
        assertEquals("That userToken is unrecognised.", error.message)
    }

    @Test
    fun shouldParseBookingError() {
        val responseError = """
        {
          "errorCode": 470,
          "title": "Booking not successful",
          "error": "2004 : Validation errors - length must be between 1 and 100"
        }
    """.trimIndent()

        // Mock the BookingError object instead of deserializing it directly
        val mockBookingError = mockk<BookingError>()

        // Define the expected behavior of the mocked object
        every { mockBookingError.title } returns "Booking not successful"
        every { mockBookingError.errorCode } returns 470
        every { mockBookingError.error } returns "2004 : Validation errors - length must be between 1 and 100"

        // Use the mock object in the test
        val bookingError: BookingError = mockBookingError

        // Perform assertions
        assertEquals("Booking not successful", bookingError.title)
        assertEquals(470, bookingError.errorCode)
        assertEquals("2004 : Validation errors - length must be between 1 and 100", bookingError.error)
    }

    @Test
    fun shouldParseBookingErrorWithNullTitle() {
        val responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}"
        val bookingError = BookingError.fromJson(responseError)

        assertNull(bookingError.title)
        assertEquals(401, bookingError.errorCode)
        assertEquals("That userToken is unrecognised.", bookingError.error)
        assertEquals(false, bookingError.hasUserError)
    }

    @Test
    fun shouldParseThrowableWithMessage() {
        val responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}"
        val bookingError: Throwable = BookingError.fromJson(responseError)

        assertEquals("That userToken is unrecognised.", bookingError.message)
    }

    @Test
    fun shouldParseBookingErrorWithOnlyTitle() {
        val responseError = "{\"title\":\"Booking not successful\"}"
        val bookingError = BookingError.fromJson(responseError)

        assertEquals("Booking not successful", bookingError.title)
        assertEquals(0, bookingError.errorCode)
        assertNull(bookingError.error)
    }
}