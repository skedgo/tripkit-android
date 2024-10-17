package com.skedgo.tripkit.booking.viewmodel

import android.annotation.SuppressLint
import com.skedgo.tripkit.booking.BookingAction
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.BookingService
import com.skedgo.tripkit.booking.FormField
import com.skedgo.tripkit.booking.FormGroup
import com.skedgo.tripkit.booking.InputForm
import com.skedgo.tripkit.booking.MockKTest
import com.skedgo.tripkit.booking.OptionFormField
import com.skedgo.tripkit.booking.StringFormField
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BookingViewModelImplTest: MockKTest() {

    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var inputForm: InputForm
    private lateinit var param: Param


    private var api: BookingService = mockk()

    @Before
    fun setUp() {
        initRx()
        bookingViewModel = BookingViewModelImpl(api)
        authenticationViewModel = AuthenticationViewModelImpl()
        inputForm = InputForm.from(arrayListOf())
        val bookingAction = BookingAction().apply { url = "url" }
        param = Param.create(bookingAction, inputForm)
    }

    @After
    fun tearDown() {
        tearDownRx()
    }

    @SuppressLint("CheckResult")
    @Test
    fun fetchNextBookingFormIfAuthenticationSucceeds() {
        authenticationViewModel = mockk()
        every { api.getFormAsync(any()) } returns Flowable.just(BookingForm())
        every { api.postFormAsync(any(), any()) } returns Flowable.just(BookingForm())
        every { authenticationViewModel.isSuccessful() } returns Observable.just(true)
        every { authenticationViewModel.verify(any()) } returns Observable.just(true)

        bookingViewModel.loadForm(param)?.blockingSingle()
        bookingViewModel.observeAuthentication(authenticationViewModel)
        authenticationViewModel.verify("www.skedgo.com").blockingSingle()

        verify(exactly = 2) { api.postFormAsync(any(), any()) }
    }

    @Test
    fun doNothingIfAuthenticationFails() {
        every { api.getFormAsync(param.url.orEmpty()) } returns Flowable.just(BookingForm())
        every { api.postFormAsync(param.url.orEmpty(), param.postBody) } returns Flowable.just(BookingForm())

        bookingViewModel.loadForm(param)?.blockingSingle()
        bookingViewModel.observeAuthentication(authenticationViewModel)
        authenticationViewModel.verify("www.google.com").blockingSingle()

        verify(exactly = 1) { api.postFormAsync("url", inputForm) }
        verify(exactly = 0) { api.getFormAsync("url") }
    }

    @Test
    fun reloadBookingForm() {
        val bookingForm = BookingForm().apply { id = "1" }
        every { api.postFormAsync(param.url.orEmpty(), param.postBody) } returns Flowable.just(bookingForm)

        bookingViewModel.loadForm(param)?.blockingSingle()
        val actual = bookingViewModel.bookingForm()?.blockingFirst()
        assertEquals("1", actual?.id)
    }

    @Test
    fun getBooking() {
        val bookingForm = BookingForm().apply { id = "2" }
        every { api.getFormAsync("url") } returns Flowable.just(bookingForm)

        bookingViewModel.loadForm(Param.create("url"))?.blockingSingle()
        val actual = bookingViewModel.bookingForm()?.blockingFirst()
        assertEquals("2", actual?.id)
    }

    @Test
    fun nextBookingForm() {
        val bookingFormItem = BookingForm()
        val formGroupList = mutableListOf<FormGroup>()

        repeat(2) {
            val bookingItemList = mutableListOf<FormField>()
            bookingItemList.add(StringFormField())
            bookingItemList.add(OptionFormField())
            formGroupList.add(FormGroup().apply { fields = bookingItemList })
        }

        bookingFormItem.form = formGroupList
        bookingFormItem.action = BookingAction().apply { url = "url2" }

        bookingViewModel.performAction(bookingFormItem)?.blockingSingle()
        bookingViewModel.nextBookingForm()?.subscribe { param ->
            assertEquals("url2", param.url)
            assertEquals(4, param.postBody?.input()?.size)
            val list = param.postBody?.input()
            assert(list?.get(0) is StringFormField)
            assert(list?.get(1) is OptionFormField)
            assert(list?.get(2) is StringFormField)
            assert(list?.get(3) is OptionFormField)
        }
    }

    @Test
    fun doNotCrashWithNullableParam() {
        try {
            val observable = bookingViewModel.loadForm(null)
            if (observable != null) {
                // If observable is not null, assert that no errors are thrown
                observable.test().apply {
                    assertNoErrors()  // Ensure no errors are thrown
                    assertComplete()   // Ensure the observable completes
                }
            } else {
                // If observable is null, just ensure that the test does not fail
                println("Observable returned null as expected")
            }
        } catch (e: NullPointerException) {
            // Catch and assert if any exceptions are thrown, for null param
            fail("Exception thrown while handling null param: ${e.message}")
        }
    }

    @Test
    fun handleCanceledBooking() {
        val bookingForm = BookingForm()
        val formGroup = FormGroup().apply {
            fields = mutableListOf(StringFormField().apply { mValue = "Cancelled" }) as List<FormField>
        }
        bookingForm.form = listOf(formGroup)
        bookingForm.action = BookingAction()

        every { api.getFormAsync(param.url.orEmpty()) } returns Flowable.just(bookingForm)

        bookingViewModel.performAction(bookingForm)
        val subscriber = bookingViewModel.isDone()?.test()
        subscriber?.assertNoErrors()
        subscriber?.assertValueSequence(listOf(false))
    }

    @Test
    fun handleSucceedBooking() {
        val bookingForm = BookingForm()
        val formGroup = FormGroup().apply {
            fields = mutableListOf(StringFormField().apply { mValue = "Confirmed" }) as List<FormField>
        }
        bookingForm.form = listOf(formGroup)
        bookingForm.action = BookingAction()

        every { api.getFormAsync(param.url.orEmpty()) } returns Flowable.just(bookingForm)

        bookingViewModel.performAction(bookingForm)
        val subscriber = bookingViewModel.isDone()?.test()
        subscriber?.assertNoErrors()
        subscriber?.assertValueSequence(listOf(true))
    }
}