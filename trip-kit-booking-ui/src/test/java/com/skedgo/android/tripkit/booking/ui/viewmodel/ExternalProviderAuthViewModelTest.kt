package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.ui.BuildConfig
import com.skedgo.android.tripkit.booking.ui.OAuth2CallbackHandler
import com.skedgo.android.tripkit.booking.ui.TestRunner
import com.skedgo.android.tripkit.booking.ui.activity.KEY_FORM
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class ExternalProviderAuthViewModelTest {

  private val viewModel: ExternalProviderAuthViewModel by lazy {
    ExternalProviderAuthViewModel()
  }

  @Test fun shouldSetArgsAuth() {
    val args = Bundle()

    val bookingForm = mock<BookingForm>()
    whenever(bookingForm.isOAuthForm).thenReturn(true)
    whenever(bookingForm.oAuthLink).thenReturn(Uri.parse("http://url"))

    args.putParcelable(KEY_FORM, bookingForm)

    viewModel.handleArgs(args)

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
    assertThat(viewModel.url.get()).isEqualTo("http://url")
  }

  @Test fun shouldSetArgsExternalAction() {
    val args = Bundle()

    val bookingForm = mock<BookingForm>()
    whenever(bookingForm.isOAuthForm).thenReturn(false)
    whenever(bookingForm.externalAction()).thenReturn("http://external_url")

    args.putParcelable(KEY_FORM, bookingForm)

    viewModel.handleArgs(args)

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
    assertThat(viewModel.url.get()).isEqualTo("http://external_url")
  }

  @Test fun shouldHandleAuthCallback() {
    val bookingForm = mock<BookingForm>()

    viewModel.bookingForm = mock<BookingForm>()

    val callbackHandler = mock<OAuth2CallbackHandler>()
    whenever(callbackHandler.handleOAuthURL(
        viewModel.bookingForm!!,
        Uri.parse("tripgo://oauth-callback?ABC"),
        "tripgo://oauth-callback"
    ))
        .thenReturn(Observable.just<BookingForm>(bookingForm))

    val shouldOverride = viewModel.handleCallback(
        "tripgo://oauth-callback?ABC",
        callbackHandler
    )
    assertThat(shouldOverride).isFalse()
    assertThat(viewModel.showWebView.get()).isFalse()
  }

  @Test fun shouldHandleRetryCallback() {
    val bookingForm = mock<BookingForm>()

    viewModel.bookingForm = mock<BookingForm>()

    val callbackHandler = mock<OAuth2CallbackHandler>()
    whenever(callbackHandler.handleRetryURL(
        viewModel.bookingForm!!,
        Uri.parse("tripgo://booking_retry")
    ))
        .thenReturn(Observable.just<BookingForm>(bookingForm))

    val shouldOverride = viewModel.handleCallback(
        "tripgo://booking_retry",
        callbackHandler
    )
    assertThat(shouldOverride).isFalse()
    assertThat(viewModel.showWebView.get()).isFalse()
  }

  @Test fun shouldHandleNoCallback() {
    val shouldOverride = viewModel.handleCallback(
        "http://www.uber.login",
        mock<OAuth2CallbackHandler>()
    )
    assertThat(shouldOverride).isTrue()
    assertThat(viewModel.showWebView.get()).isTrue()
  }

  @Test fun shouldHandleBookingFormAuth() {
    val bookingForm = mock<BookingForm>()
    whenever(bookingForm.isOAuthForm).thenReturn(true)
    whenever(bookingForm.oAuthLink).thenReturn(Uri.parse("http://url"))

    val subscriber = TestSubscriber<Intent>()

    viewModel.handledForm(bookingForm).subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoValues()
    subscriber.assertNoErrors()

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
    assertThat(viewModel.url.get()).isEqualTo("http://url")
  }

  @Test fun shouldHandleNullBookingForm() {

    val subscriber = TestSubscriber<Intent>()
    viewModel.handledForm(null).subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()

    val intent = subscriber.onNextEvents[0]

    val bookingFormResult = intent.getParcelableExtra<BookingForm>(KEY_FORM)
    assertThat(bookingFormResult).isNull()
  }

  @Test fun shouldHandleBookingFormAuthNoLink() {
    val bookingForm = mock<BookingForm>()
    whenever(bookingForm.isOAuthForm).thenReturn(false)
    whenever(bookingForm.oAuthLink).thenReturn(null)

    val subscriber = TestSubscriber<Intent>()

    viewModel.handledForm(bookingForm).subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()

    val intent = subscriber.onNextEvents[0]

    val bookingFormResult = intent.getParcelableExtra<BookingForm>(KEY_FORM)
    assertThat(bookingFormResult).isEqualTo(bookingForm)
  }

  @Test fun shouldHandleBookingFormAuthLink() {
    val bookingForm = mock<BookingForm>()
    whenever(bookingForm.isOAuthForm).thenReturn(true)
    whenever(bookingForm.oAuthLink).thenReturn(Uri.parse("http://url"))

    val subscriber = TestSubscriber<Intent>()

    viewModel.handledForm(bookingForm).subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoValues()
    subscriber.assertNoErrors()

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
    assertThat(viewModel.url.get()).isEqualTo("http://url")
  }
}