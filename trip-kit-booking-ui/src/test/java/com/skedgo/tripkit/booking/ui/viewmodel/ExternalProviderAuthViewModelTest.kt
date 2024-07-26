package com.skedgo.tripkit.booking.ui.viewmodel

import android.net.Uri
import android.os.Bundle
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.ui.OAuth2CallbackHandler
import com.skedgo.tripkit.booking.ui.activity.KEY_FORM
import io.reactivex.Observable
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExternalProviderAuthViewModelTest {

    private val viewModel: ExternalProviderAuthViewModel by lazy {
        ExternalProviderAuthViewModel()
    }

    @Test
    fun shouldSetArgsAuth() {
        val args = Bundle()

        val bookingForm = mock<BookingForm>()
        whenever(bookingForm.isOAuthForm).thenReturn(true)
        whenever(bookingForm.oAuthLink).thenReturn(Uri.parse("http://url"))

        args.putParcelable(KEY_FORM, bookingForm)

        viewModel.handleArgs(args)

        assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
        assertThat(viewModel.url.get()).isEqualTo("http://url")
    }

    @Test
    fun shouldSetArgsExternalAction() {
        val args = Bundle()

        val bookingForm = mock<BookingForm>()
        whenever(bookingForm.isOAuthForm).thenReturn(false)
        whenever(bookingForm.externalAction()).thenReturn("http://external_url")

        args.putParcelable(KEY_FORM, bookingForm)

        viewModel.handleArgs(args)

        assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
        assertThat(viewModel.url.get()).isEqualTo("http://external_url")
    }

    @Test
    fun shouldHandleAuthCallback() {
        val bookingForm = mock<BookingForm>()

        viewModel.bookingForm = mock<BookingForm>()

        val callbackHandler = mock<OAuth2CallbackHandler>()
        whenever(
            callbackHandler.handleOAuthURL(
                viewModel.bookingForm!!,
                Uri.parse("tripgo://oauth-callback?ABC"),
                "tripgo://oauth-callback"
            )
        )
            .thenReturn(Observable.just<BookingForm>(bookingForm))

        val shouldOverride = viewModel.handleCallback(
            "tripgo://oauth-callback?ABC",
            callbackHandler
        )
        assertThat(shouldOverride).isFalse()
        assertThat(viewModel.showWebView.get()).isFalse()
    }

    @Test
    fun shouldHandleRetryCallback() {
        val bookingForm = mock<BookingForm>()

        viewModel.bookingForm = mock<BookingForm>()

        val callbackHandler = mock<OAuth2CallbackHandler>()
        whenever(
            callbackHandler.handleRetryURL(
                viewModel.bookingForm!!,
                Uri.parse("tripgo://booking_retry")
            )
        )
            .thenReturn(Observable.just<BookingForm>(bookingForm))

        val shouldOverride = viewModel.handleCallback(
            "tripgo://booking_retry",
            callbackHandler
        )
        assertThat(shouldOverride).isFalse()
        assertThat(viewModel.showWebView.get()).isFalse()

        verify(callbackHandler).handleRetryURL(
            viewModel.bookingForm!!,
            Uri.parse("tripgo://booking_retry")
        )
    }

    @Test
    fun shouldHandleNoCallback() {
        val shouldOverride = viewModel.handleCallback(
            "http://www.uber.login",
            mock<OAuth2CallbackHandler>()
        )
        assertThat(shouldOverride).isTrue()
        assertThat(viewModel.showWebView.get()).isTrue()
    }

    @Test
    fun shouldHandleBookingFormAuth() {
        val bookingForm = mock<BookingForm>()
        whenever(bookingForm.isOAuthForm).thenReturn(true)
        whenever(bookingForm.oAuthLink).thenReturn(Uri.parse("http://url"))

        val subscriber = viewModel.handledForm(bookingForm).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoValues()
        subscriber.assertNoErrors()

        assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
        assertThat(viewModel.url.get()).isEqualTo("http://url")
    }

    @Test
    fun shouldHandleNullBookingForm() {

        val subscriber = viewModel.handledForm(null).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        val intent = subscriber.values()[0]

        val bookingFormResult = intent.getParcelableExtra<BookingForm>(KEY_FORM)
        assertThat(bookingFormResult).isNull()
    }

    @Test
    fun shouldHandleBookingFormAuthNoLink() {
        val bookingForm = mock<BookingForm>()
        whenever(bookingForm.isOAuthForm).thenReturn(false)
        whenever(bookingForm.oAuthLink).thenReturn(null)

        val subscriber = viewModel.handledForm(bookingForm).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        val intent = subscriber.values()[0]

        val bookingFormResult = intent.getParcelableExtra<BookingForm>(KEY_FORM)
        assertThat(bookingFormResult).isEqualTo(bookingForm)
    }

    @Test
    fun shouldHandleBookingFormAuthLink() {
        val bookingForm = mock<BookingForm>()
        whenever(bookingForm.isOAuthForm).thenReturn(true)
        whenever(bookingForm.oAuthLink).thenReturn(Uri.parse("http://url"))

        val subscriber = viewModel.handledForm(bookingForm).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoValues()
        subscriber.assertNoErrors()

        assertThat(viewModel.bookingForm).isEqualTo(bookingForm)
        assertThat(viewModel.url.get()).isEqualTo("http://url")
    }
}