package com.skedgo.android.tripkit.booking.ui.viewmodel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.ui.BuildConfig;
import com.skedgo.android.tripkit.booking.ui.OAuth2CallbackHandler;
import com.skedgo.android.tripkit.booking.ui.TestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.skedgo.android.tripkit.booking.ui.activity.BookingActivity.KEY_BOOKING_FORM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class ExternalProviderAuthViewModelTest {
  private ExternalProviderAuthViewModel viewModel;

  @Before public void before() {
    viewModel = new ExternalProviderAuthViewModel();
  }

  @Test public void shouldSetArgsAuth() {
    final Bundle args = new Bundle();

    BookingForm bookingForm = mock(BookingForm.class);
    when(bookingForm.isOAuthForm()).thenReturn(true);
    when(bookingForm.getOAuthLink()).thenReturn(Uri.parse("http://url"));

    args.putParcelable(KEY_BOOKING_FORM, bookingForm);

    viewModel.handleArgs(args);

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm);
    assertThat(viewModel.url().get()).isEqualTo("http://url");
  }

  @Test public void shouldSetArgsExternalAction() {
    final Bundle args = new Bundle();

    BookingForm bookingForm = mock(BookingForm.class);
    when(bookingForm.isOAuthForm()).thenReturn(false);
    when(bookingForm.externalAction()).thenReturn("http://external_url");

    args.putParcelable(KEY_BOOKING_FORM, bookingForm);

    viewModel.handleArgs(args);

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm);
    assertThat(viewModel.url().get()).isEqualTo("http://external_url");
  }

  @Test public void shouldHandleAuthCallback() {
    BookingForm bookingForm = mock(BookingForm.class);

    viewModel.bookingForm = mock(BookingForm.class);

    OAuth2CallbackHandler callbackHandler = mock(OAuth2CallbackHandler.class);
    when(callbackHandler.handleOAuthURL(
        viewModel.bookingForm,
        Uri.parse("tripgo://oauth-callback?ABC"),
        "tripgo://oauth-callback"
    ))
        .thenReturn(Observable.just(bookingForm));

    boolean shouldOverride = viewModel.handleCallback(
        "tripgo://oauth-callback?ABC",
        callbackHandler
    );
    assertThat(shouldOverride).isFalse();
    assertThat(viewModel.showWebView().get()).isFalse();
  }

  @Test public void shouldHandleRetryCallback() {
    BookingForm bookingForm = mock(BookingForm.class);

    viewModel.bookingForm = mock(BookingForm.class);

    OAuth2CallbackHandler callbackHandler = mock(OAuth2CallbackHandler.class);
    when(callbackHandler.handleRetryURL(
        viewModel.bookingForm,
        Uri.parse("tripgo://booking_retry")
    ))
        .thenReturn(Observable.just(bookingForm));

    boolean shouldOverride = viewModel.handleCallback(
        "tripgo://booking_retry",
        callbackHandler
    );
    assertThat(shouldOverride).isFalse();
    assertThat(viewModel.showWebView().get()).isFalse();
  }

  @Test public void shouldHandleNoCallback() {
    boolean shouldOverride = viewModel.handleCallback(
        "http://www.uber.login",
        mock(OAuth2CallbackHandler.class)
    );
    assertThat(shouldOverride).isTrue();
    assertThat(viewModel.showWebView().get()).isTrue();
  }

  @Test public void shouldHandleBookingFormAuth() {
    BookingForm bookingForm = mock(BookingForm.class);
    when(bookingForm.isOAuthForm()).thenReturn(true);
    when(bookingForm.getOAuthLink()).thenReturn(Uri.parse("http://url"));

    TestSubscriber<Intent> subscriber = new TestSubscriber<>();

    viewModel.handledForm(bookingForm).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoValues();
    subscriber.assertNoErrors();

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm);
    assertThat(viewModel.url().get()).isEqualTo("http://url");
  }

  @Test public void shouldHandleNullBookingForm() {

    TestSubscriber<Intent> subscriber = new TestSubscriber<>();
    viewModel.handledForm(null).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    Intent intent = subscriber.getOnNextEvents().get(0);

    BookingForm bookingFormResult = intent.getParcelableExtra(KEY_BOOKING_FORM);
    assertThat(bookingFormResult).isNull();
  }

  @Test public void shouldHandleBookingFormAuthNoLink() {
    BookingForm bookingForm = mock(BookingForm.class);
    when(bookingForm.isOAuthForm()).thenReturn(false);
    when(bookingForm.getOAuthLink()).thenReturn(null);

    TestSubscriber<Intent> subscriber = new TestSubscriber<>();

    viewModel.handledForm(bookingForm).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    Intent intent = subscriber.getOnNextEvents().get(0);

    BookingForm bookingFormResult = intent.getParcelableExtra(KEY_BOOKING_FORM);
    assertThat(bookingFormResult).isEqualTo(bookingForm);
  }

  @Test public void shouldHandleBookingFormAuthLink() {
    BookingForm bookingForm = mock(BookingForm.class);
    when(bookingForm.isOAuthForm()).thenReturn(true);
    when(bookingForm.getOAuthLink()).thenReturn(Uri.parse("http://url"));

    TestSubscriber<Intent> subscriber = new TestSubscriber<>();

    viewModel.handledForm(bookingForm).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoValues();
    subscriber.assertNoErrors();

    assertThat(viewModel.bookingForm).isEqualTo(bookingForm);
    assertThat(viewModel.url().get()).isEqualTo("http://url");
  }
}
