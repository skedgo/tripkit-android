package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.content.res.Resources
import android.os.Bundle
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.*
import com.skedgo.android.tripkit.booking.ui.BuildConfig
import com.skedgo.android.tripkit.booking.ui.TestRunner
import com.skedgo.android.tripkit.booking.ui.activity.*
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromAction
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromUrl
import com.skedgo.android.tripkit.booking.ui.usecase.IsCancelAction
import com.skedgo.android.tripkit.booking.ui.usecase.IsDoneAction
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.Observable
import rx.observers.TestSubscriber
import java.net.SocketTimeoutException


@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class BookingFormViewModelTest {

  private val resources: Resources = RuntimeEnvironment.application.resources
  private val getBookingFormFromUrl: GetBookingFormFromUrl = mock()
  private val getBookingFormFromAction: GetBookingFormFromAction = mock()
  private val isCancelAction: IsCancelAction = mock()
  private val isDoneAction: IsDoneAction = mock()

  private val viewModel: BookingFormViewModel by lazy {
    BookingFormViewModel(resources, getBookingFormFromUrl,
        getBookingFormFromAction, isCancelAction, isDoneAction)
  }

  @Test
  fun shouldActionEmitCancel() {
    val bookingForm = mock<BookingForm>()

    whenever(isCancelAction.execute(bookingForm)).thenReturn(true)
    whenever(isDoneAction.execute(bookingForm)).thenReturn(true)

    val subscriber = TestSubscriber<Boolean>()
    viewModel.onCancel.subscribe(subscriber)

    viewModel.bookingForm = bookingForm

    viewModel.onAction()

    subscriber.assertValue(true)

  }

  @Test
  fun shouldActionEmitDone() {
    val bookingForm = mock<BookingForm>()

    whenever(isCancelAction.execute(bookingForm)).thenReturn(false)
    whenever(isDoneAction.execute(bookingForm)).thenReturn(true)

    val subscriber = TestSubscriber<BookingForm>()
    viewModel.onDone.subscribe(subscriber)

    viewModel.bookingForm = bookingForm

    viewModel.onAction()

    subscriber.assertValue(bookingForm)

  }

  @Test
  fun shouldActionEmitNextBookingForm() {
    val bookingForm = mock<BookingForm>()

    whenever(isCancelAction.execute(bookingForm)).thenReturn(false)
    whenever(isDoneAction.execute(bookingForm)).thenReturn(false)

    val subscriber = TestSubscriber<BookingForm>()
    viewModel.onNextBookingFormAction.subscribe(subscriber)

    viewModel.bookingForm = bookingForm

    viewModel.onAction()

    subscriber.assertValue(bookingForm)

  }

  @Test
  fun shouldEmitRetry() {
    val bookingError = mock<BookingError>()
    whenever(bookingError.url).thenReturn("retry url")

    val subscriber = TestSubscriber<String>()
    viewModel.onErrorRetry.subscribe(subscriber)

    viewModel.bookingError = bookingError

    viewModel.onRetry()

    subscriber.assertValue("retry url")

  }

  @Test
  fun shouldEmitCancel() {
    val subscriber = TestSubscriber<Boolean>()
    viewModel.onCancel.subscribe(subscriber)

    viewModel.onCancel()

    subscriber.assertValue(true)

  }

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

  @Test
  fun shouldUpdateBookingFormInfoWithAction() {
    val bookingForm = mock<BookingForm>()
    val action = mock<BookingAction>()
    whenever(bookingForm.action).thenReturn(action)
    whenever(action.title).thenReturn("action title")
    whenever(bookingForm.title).thenReturn("booking title")

    viewModel.bookingForm = bookingForm

    val subscriber = TestSubscriber<String>()
    viewModel.onUpdateFormTitle.subscribe(subscriber)

    viewModel.updateBookingFormInfo()

    subscriber.assertValue("booking title")

    assertThat(viewModel.showAction.get()).isTrue()
    assertThat(viewModel.actionTitle.get()).isEqualTo("action title")

  }

  @Test
  fun shouldUpdateBookingFormInfoWithNoAction() {
    val bookingForm = mock<BookingForm>()
    whenever(bookingForm.action).thenReturn(null)
    whenever(bookingForm.title).thenReturn("booking title")

    viewModel.bookingForm = bookingForm

    val subscriber = TestSubscriber<String>()
    viewModel.onUpdateFormTitle.subscribe(subscriber)

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
    whenever(formGroup.fields).thenReturn(listOf(stringField, passwordField, externalField, bookingFormField))

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

  @Test
  fun shouldEmitErrorOnFetchBookingForm() {
    val bundle = Bundle()
    val subscriber = TestSubscriber<Any?>()

    viewModel.fetchBookingFormAsync(bundle).subscribe(subscriber)

    subscriber.assertError(Error::class.java)
  }

  @Test
  fun shouldEmitBundleFormOnFetchBookingForm() {
    val bookingForm = mock<BookingForm>()

    val bundle = Bundle()
    bundle.putInt(KEY_TYPE, BOOKING_TYPE_FORM)
    bundle.putParcelable(KEY_FORM, bookingForm)

    val subscriber = TestSubscriber<Any?>()

    viewModel.fetchBookingFormAsync(bundle).subscribe(subscriber)

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

    val subscriber = TestSubscriber<Any?>()

    viewModel.fetchBookingFormAsync(bundle).subscribe(subscriber)

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

    whenever(getBookingFormFromAction.execute(bookingFormAction)).thenReturn(Observable.just(bookingForm))

    val subscriber = TestSubscriber<Any?>()

    viewModel.fetchBookingFormAsync(bundle).subscribe(subscriber)

    subscriber.assertValue(bookingForm)
    assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
  }

  @Test
  fun shouldEmitDoneOnFetchBookingForm() {
    val bookingFormAction = mock<BookingForm>()

    val bundle = Bundle()
    bundle.putInt(KEY_TYPE, BOOKING_TYPE_ACTION)
    bundle.putParcelable(KEY_FORM, bookingFormAction)

    whenever(getBookingFormFromAction.execute(bookingFormAction)).thenReturn(Observable.just(null))

    val subscriber = TestSubscriber<Any?>()
    val subscriberDone = TestSubscriber<Any?>()

    viewModel.onDone.subscribe(subscriberDone)
    viewModel.fetchBookingFormAsync(bundle).subscribe(subscriber)

    subscriber.assertValue(null)
    subscriberDone.assertValue(null)
  }

  @Test
  fun shouldEmitDoneOnFetchNullBookingForm() {
    val bookingFormAction = mock<BookingForm>()

    val bundle = Bundle()
    bundle.putInt(KEY_TYPE, BOOKING_TYPE_ACTION)
    bundle.putParcelable(KEY_FORM, bookingFormAction)

    whenever(getBookingFormFromAction.execute(bookingFormAction)).thenReturn(Observable.just(NullBookingForm))

    val subscriber = TestSubscriber<Any?>()
    val subscriberDone = TestSubscriber<Any?>()

    viewModel.onDone.subscribe(subscriberDone)
    viewModel.fetchBookingFormAsync(bundle).subscribe(subscriber)

    subscriber.assertValue(NullBookingForm)
    subscriberDone.assertValue(NullBookingForm)
  }

  @Test
  fun `should show error for network error`() {
    val bundle = Bundle()
    bundle.putInt(KEY_TYPE, BOOKING_TYPE_URL)
    bundle.putString(KEY_URL, "abc")

    whenever(getBookingFormFromUrl.execute("abc")).thenReturn(Observable.error(SocketTimeoutException()))
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