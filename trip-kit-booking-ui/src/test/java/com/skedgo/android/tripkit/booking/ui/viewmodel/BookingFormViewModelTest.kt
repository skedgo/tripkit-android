package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.content.res.Resources
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.BookingAction
import com.skedgo.android.tripkit.booking.BookingError
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.R
import com.skedgo.android.tripkit.booking.ui.BuildConfig
import com.skedgo.android.tripkit.booking.ui.TestRunner
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromAction
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromUrl
import com.skedgo.android.tripkit.booking.ui.usecase.IsCancelAction
import com.skedgo.android.tripkit.booking.ui.usecase.IsDoneAction
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import rx.observers.TestSubscriber


@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class BookingFormViewModelTest {

  private val resources: Resources = mock()
  private val getBookingFormFromUrl: GetBookingFormFromUrl = mock()
  private val getBookingFormFromAction: GetBookingFormFromAction = mock()
  private val isCancelAction: IsCancelAction = mock()
  private val isDoneAction: IsDoneAction = mock()

  private val viewModel: BookingFormViewModel by lazy {
    BookingFormViewModel(resources, getBookingFormFromUrl,
        getBookingFormFromAction, isCancelAction, isDoneAction)
  }

  @Test fun shouldActionEmitCancel() {
    val bookingForm = mock<BookingForm>()

    whenever(isCancelAction.execute(bookingForm)).thenReturn(true)
    whenever(isDoneAction.execute(bookingForm)).thenReturn(true)

    val subscriber = TestSubscriber<Boolean>()
    viewModel.onCancel.subscribe(subscriber)

    viewModel.bookingForm = bookingForm

    viewModel.onAction()

    subscriber.assertValue(true)

  }

  @Test fun shouldActionEmitDone() {
    val bookingForm = mock<BookingForm>()

    whenever(isCancelAction.execute(bookingForm)).thenReturn(false)
    whenever(isDoneAction.execute(bookingForm)).thenReturn(true)

    val subscriber = TestSubscriber<BookingForm>()
    viewModel.onDone.subscribe(subscriber)

    viewModel.bookingForm = bookingForm

    viewModel.onAction()

    subscriber.assertValue(bookingForm)

  }

  @Test fun shouldActionEmitNextBookingForm() {
    val bookingForm = mock<BookingForm>()

    whenever(isCancelAction.execute(bookingForm)).thenReturn(false)
    whenever(isDoneAction.execute(bookingForm)).thenReturn(false)

    val subscriber = TestSubscriber<BookingForm>()
    viewModel.onNextBookingFormAction.subscribe(subscriber)

    viewModel.bookingForm = bookingForm

    viewModel.onAction()

    subscriber.assertValue(bookingForm)

  }

  @Test fun shouldEmitRetry() {
    val bookingError = mock<BookingError>()
    whenever(bookingError.url).thenReturn("retry url")

    val subscriber = TestSubscriber<String>()
    viewModel.onErrorRetry.subscribe(subscriber)

    viewModel.bookingError = bookingError

    viewModel.onRetry()

    subscriber.assertValue("retry url")

  }

  @Test fun shouldEmitCancel() {
    val subscriber = TestSubscriber<Boolean>()
    viewModel.onCancel.subscribe(subscriber)

    viewModel.onCancel()

    subscriber.assertValue(true)

  }

  @Test fun shouldShowRecoveryError() {
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

  @Test fun shouldShowRetryError() {
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

  @Test fun shouldUpdateBookingFormInfoWithAction() {
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

  @Test fun shouldUpdateBookingFormInfoWithNoAction() {
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

}