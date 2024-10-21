package com.skedgo.tripkit.booking.ui.viewmodel

import android.content.res.Resources
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.BookingAction
import com.skedgo.tripkit.booking.BookingError
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.ExternalFormField
import com.skedgo.tripkit.booking.FormGroup
import com.skedgo.tripkit.booking.NullBookingForm
import com.skedgo.tripkit.booking.PasswordFormField
import com.skedgo.tripkit.booking.R
import com.skedgo.tripkit.booking.StringFormField
import com.skedgo.tripkit.booking.ui.activity.BOOKING_TYPE_ACTION
import com.skedgo.tripkit.booking.ui.activity.BOOKING_TYPE_FORM
import com.skedgo.tripkit.booking.ui.activity.BOOKING_TYPE_URL
import com.skedgo.tripkit.booking.ui.activity.KEY_FORM
import com.skedgo.tripkit.booking.ui.activity.KEY_TYPE
import com.skedgo.tripkit.booking.ui.activity.KEY_URL
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.booking.ui.usecase.GetBookingFormFromAction
import com.skedgo.tripkit.booking.ui.usecase.GetBookingFormFromUrl
import com.skedgo.tripkit.booking.ui.usecase.IsCancelAction
import com.skedgo.tripkit.booking.ui.usecase.IsDoneAction
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.SocketTimeoutException


@RunWith(AndroidJUnit4::class)
class BookingFormViewModelTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        initRx()
    }

    @After
    fun tearDown() {
        tearDownRx()
    }

    private val resources: Resources =
        (ApplicationProvider.getApplicationContext() as android.content.Context).resources
    private val getBookingFormFromUrl: GetBookingFormFromUrl = mock()
    private val getBookingFormFromAction: GetBookingFormFromAction = mock()
    private val isCancelAction: IsCancelAction = mock()
    private val isDoneAction: IsDoneAction = mock()

    private val viewModel: BookingFormViewModel by lazy {
        BookingFormViewModel(
            resources, getBookingFormFromUrl,
            getBookingFormFromAction, isCancelAction, isDoneAction
        )
    }

    @Test
    fun shouldActionEmitCancel() {
        val bookingForm = mock<BookingForm>()

        whenever(isCancelAction.execute(bookingForm)).thenReturn(true)
        whenever(isDoneAction.execute(bookingForm)).thenReturn(true)

        val subscriber = viewModel.onCancel.test()

        viewModel.bookingForm = bookingForm

        viewModel.onAction()

        subscriber.assertValue(true)

    }

    @Test
    fun shouldActionEmitDone() {
        val bookingForm = mock<BookingForm>()

        whenever(isCancelAction.execute(bookingForm)).thenReturn(false)
        whenever(isDoneAction.execute(bookingForm)).thenReturn(true)

        val subscriber = viewModel.onDone.test()

        viewModel.bookingForm = bookingForm

        viewModel.onAction()

        subscriber.assertValue(bookingForm)

    }

    @Test
    fun shouldActionEmitNextBookingForm() {
        val bookingForm = mock<BookingForm>()

        whenever(isCancelAction.execute(bookingForm)).thenReturn(false)
        whenever(isDoneAction.execute(bookingForm)).thenReturn(false)

        val subscriber = viewModel.onNextBookingFormAction.test()

        viewModel.bookingForm = bookingForm

        viewModel.onAction()

        subscriber.assertValue(bookingForm)

    }

    // TODO: Unit test - refactor
    /* Disabled function due to mockito exception
    @Test
    fun shouldEmitRetry() {
        val bookingError = mock<BookingError>()
        whenever(bookingError.url).thenReturn("retry url")

        val subscriber = viewModel.onErrorRetry.test()

        viewModel.bookingError = bookingError

        viewModel.onRetry()

        subscriber.assertValue("retry url")

    }
     */

    @Test
    fun shouldEmitCancel() {
        val subscriber = viewModel.onCancel.test()

        viewModel.onCancel()

        subscriber.assertValue(true)

    }

    // TODO: Unit test - refactor
    /* Disabled functions due to mockito exception
    @Test
    fun shouldShowRecoveryError() {
        val bookingError = mock<BookingError>()
        whenever(bookingError.title).thenReturn("error title")
        whenever(bookingError.error).thenReturn("error message")
        whenever(bookingError.recoveryTitle).thenReturn("recovery title")
        whenever(bookingError.url).thenReturn("retry url")

        viewModel.showError(bookingError)

        assertThat(viewModel.hasError.get()).isTrue()
        assertThat(viewModel.errorTitle.get()).isEqualTo("error title")
        assertThat(viewModel.errorMessage.get()).isEqualTo("error message")
        assertThat(viewModel.retryText.get()).isEqualTo("recovery title")
        assertThat(viewModel.showRetry.get()).isTrue()

    }

    @Test
    fun shouldShowRetryError() {
        val bookingError = mock<BookingError>()
        whenever(bookingError.title).thenReturn("error title")
        whenever(bookingError.error).thenReturn("error message")
        whenever(bookingError.recoveryTitle).thenReturn(null)
        whenever(bookingError.url).thenReturn(null)

        viewModel.showError(bookingError)

        assertThat(viewModel.hasError.get()).isTrue()
        assertThat(viewModel.errorTitle.get()).isEqualTo("error title")
        assertThat(viewModel.errorMessage.get()).isEqualTo("error message")
        assertThat(viewModel.retryText.get()).isEqualTo(resources.getString(R.string.retry))
        assertThat(viewModel.showRetry.get()).isFalse()

    }
     */

    // TODO: Unit test - refactor
    /* Disabled function due to missing method invocation exception
    @Test
    fun shouldUpdateBookingFormInfoWithAction() {
        val bookingForm = mock<BookingForm>()
        val action = mock<BookingAction>()
        whenever(bookingForm.action).thenReturn(action)
        whenever(action.title).thenReturn("action title")
        whenever(bookingForm.title).thenReturn("booking title")

        viewModel.bookingForm = bookingForm

        val subscriber = viewModel.onUpdateFormTitle.test()

        viewModel.updateBookingFormInfo()

        subscriber.assertValue("booking title")

        assertThat(viewModel.showAction.get()).isTrue()
        assertThat(viewModel.actionTitle.get()).isEqualTo("action title")

    }
     */

    // TODO: Unit test - refactor
    /* Disabled functions due to mockito exception
    @Test
    fun shouldUpdateBookingFormInfoWithNoAction() {
        val bookingForm = mock<BookingForm>()
        whenever(bookingForm.action).thenReturn(null)
        whenever(bookingForm.title).thenReturn("booking title")

        viewModel.bookingForm = bookingForm

        val subscriber = viewModel.onUpdateFormTitle.test()

        viewModel.updateBookingFormInfo()

        subscriber.assertValue("booking title")

        assertThat(viewModel.showAction.get()).isFalse()
        assertThat(viewModel.actionTitle.get()).isNull()

    }

    @Test
    fun shouldUpdateFieldList() {
        val bookingForm = mock<BookingForm>()
        val formGroup = mock<FormGroup>()
        val stringField = mock<StringFormField>()
        val passwordField = mock<PasswordFormField>()
        val externalField = mock<ExternalFormField>()
        val bookingFormField = mock<BookingForm>()

        whenever(bookingForm.form).thenReturn(listOf(formGroup))
        whenever(formGroup.title).thenReturn("group title")
        whenever(formGroup.footer).thenReturn("group footer")
        whenever(formGroup.fields).thenReturn(
            listOf(
                stringField,
                passwordField,
                externalField,
                bookingFormField
            )
        )

        viewModel.bookingForm = bookingForm

        viewModel.updateFieldList()

        assertThat(viewModel.items).hasSize(6)
        assertThat(viewModel.items).contains("group title")
        assertThat(viewModel.items).contains("group footer")
        assertThat(viewModel.items).hasAtLeastOneElementOfType(FieldStringViewModel::class.java)
        assertThat(viewModel.items).hasAtLeastOneElementOfType(FieldPasswordViewModel::class.java)
        assertThat(viewModel.items).hasAtLeastOneElementOfType(FieldExternalViewModel::class.java)
        assertThat(viewModel.items).hasAtLeastOneElementOfType(FieldBookingFormViewModel::class.java)

    }
    */

    @Test
    fun shouldEmitErrorOnFetchBookingForm() {
        val viewModel = mockk<BookingFormViewModel>()
        val bundle = mockk<Bundle>()
        every { viewModel.fetchBookingFormAsync(bundle) } returns Observable.error(Exception())
        val subscriber = viewModel.fetchBookingFormAsync(bundle).test()
        subscriber.assertError(Exception::class.java)
    }

    @Test
    fun shouldEmitBundleFormOnFetchBookingForm() {
        val bookingForm = mock<BookingForm>()

        val bundle = Bundle()
        bundle.putInt(KEY_TYPE, BOOKING_TYPE_FORM)
        bundle.putParcelable(KEY_FORM, bookingForm)

        val subscriber = viewModel.fetchBookingFormAsync(bundle).test()

        subscriber.assertValue(bookingForm)
        assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
    }

    @Test
    fun shouldEmitBookingFormFromUrlOnFetchBookingForm() {
        val bookingForm = mock<BookingForm>()

        val bundle = Bundle()
        bundle.putInt(KEY_TYPE, BOOKING_TYPE_URL)
        bundle.putString(KEY_URL, "url")

        whenever(getBookingFormFromUrl.execute("url")).thenReturn(Observable.just(bookingForm))

        val subscriber = viewModel.fetchBookingFormAsync(bundle).test()

        subscriber.assertValue(bookingForm)
        assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
    }

    @Test
    fun shouldEmitBookingFormFromActionOnFetchBookingForm() {
        val bookingForm = mock<BookingForm>()
        val bookingFormAction = mock<BookingForm>()

        val bundle = Bundle()
        bundle.putInt(KEY_TYPE, BOOKING_TYPE_ACTION)
        bundle.putParcelable(KEY_FORM, bookingFormAction)

        whenever(getBookingFormFromAction.execute(bookingFormAction)).thenReturn(
            Observable.just(
                bookingForm
            )
        )

        val subscriber = viewModel.fetchBookingFormAsync(bundle).test()

        subscriber.assertValue(bookingForm)
        assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
    }

    @Test
    fun shouldEmitDoneOnFetchBookingForm() {
        val bookingFormAction = mock<BookingForm>()

        val bundle = Bundle()
        bundle.putInt(KEY_TYPE, BOOKING_TYPE_ACTION)
        bundle.putParcelable(KEY_FORM, bookingFormAction)

        whenever(getBookingFormFromAction.execute(bookingFormAction)).thenReturn(Observable.empty())

        val subscriberDone = viewModel.onDone.test()
        val subscriber = viewModel.fetchBookingFormAsync(bundle).test()

        subscriber.assertComplete()
        subscriberDone.assertEmpty()
    }

    @Test
    fun shouldEmitDoneOnFetchNullBookingForm() {
        val bookingFormAction = mock<BookingForm>()

        val bundle = Bundle()
        bundle.putInt(KEY_TYPE, BOOKING_TYPE_ACTION)
        bundle.putParcelable(KEY_FORM, bookingFormAction)

        whenever(getBookingFormFromAction.execute(bookingFormAction)).thenReturn(
            Observable.just(
                NullBookingForm
            )
        )

        val subscriberDone = viewModel.onDone.test()
        val subscriber = viewModel.fetchBookingFormAsync(bundle).test()

        subscriber.assertValue(NullBookingForm)
        subscriberDone.assertValue(NullBookingForm)
    }

    @Test
    fun `should show error for network error`() {
        val bundle = Bundle()
        bundle.putInt(KEY_TYPE, BOOKING_TYPE_URL)
        bundle.putString(KEY_URL, "abc")

        whenever(getBookingFormFromUrl.execute("abc")).thenReturn(
            Observable.error(
                SocketTimeoutException()
            )
        )
        viewModel.fetchBookingFormAsync(bundle)
            .test()
            .assertNoErrors()

        assertThat(viewModel.hasError.get()).isTrue()
        assertThat(viewModel.bookingError).isNull()
        assertThat(viewModel.errorTitle.get()).isEqualTo("An unexpected network error has occurred.")
        assertThat(viewModel.errorMessage.get()).isNull()
        assertThat(viewModel.showRetry.get()).isTrue()
    }
}