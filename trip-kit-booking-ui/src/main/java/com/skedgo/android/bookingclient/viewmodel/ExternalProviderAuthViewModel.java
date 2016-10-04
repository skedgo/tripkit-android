package com.skedgo.android.bookingclient.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.skedgo.android.bookingclient.OAuth2CallbackHandler;
import com.skedgo.android.tripkit.booking.BookingForm;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import skedgo.common.BaseViewModel;

import static com.skedgo.android.bookingclient.activity.ExternalProviderAuthActivity.KEY_BOOKING_FORM;

public class ExternalProviderAuthViewModel extends BaseViewModel {
  private final ObservableBoolean showWebView;
  private final PublishSubject<Intent> publishSubjectIntent = PublishSubject.create();
  private final ObservableField<String> url;

  @Nullable @VisibleForTesting BookingForm bookingForm;

  @Inject ExternalProviderAuthViewModel() {
    showWebView = new ObservableBoolean(false);
    url = new ObservableField<>();
  }

  public ObservableBoolean showWebView() {
    return showWebView;
  }

  public Observable<Intent> intentObservable() {
    return publishSubjectIntent.asObservable();
  }

  public ObservableField<String> url() {
    return url;
  }

  public void handleArgs(Bundle args) {
    BookingForm form = args.getParcelable(KEY_BOOKING_FORM);
    handleArgs(form);
  }

  private void handleArgs(BookingForm form) {
    this.bookingForm = form;

    if (bookingForm != null) {
      if (bookingForm.isOAuthForm() && bookingForm.getOAuthLink() != null) {
        url.set(bookingForm.getOAuthLink().toString());
      } else if (bookingForm.externalAction() != null) {
        url.set(bookingForm.externalAction());
      }
    }
  }

  public WebViewClient webViewClient(final OAuth2CallbackHandler oAuth2CallbackHandler) {
    return new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return handleCallback(url, oAuth2CallbackHandler);
      }

      @Override
      public void onPageFinished(WebView view, final String url) {
        if (!url.startsWith("tripgo://")) {
          showWebView.set(true);
        }
      }
    };
  }

  @VisibleForTesting
  boolean handleCallback(String webUrl, OAuth2CallbackHandler oAuth2CallbackHandler) {
    if (webUrl.startsWith("tripgo://")) {
      if (bookingForm != null) {
        if (webUrl.startsWith("tripgo://oauth-callback")) {
          String callback = webUrl.substring(0, webUrl.indexOf("?"));

          oAuth2CallbackHandler.handleOAuthURL(bookingForm, Uri.parse(webUrl), callback)
              .takeUntil(destroyed())
              .flatMap(new Func1<BookingForm, Observable<Intent>>() {
                @Override public Observable<Intent> call(BookingForm bookingForm) {
                  return handledForm(bookingForm);
                }
              })
              .subscribe(new Action1<Intent>() {
                @Override public void call(Intent intent) {
                  publishSubjectIntent.onNext(intent);
                }
              }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                  publishSubjectIntent.onError(throwable);
                }
              });
        } else if (webUrl.startsWith("tripgo://booking_retry")) {
          oAuth2CallbackHandler.handleRetryURL(bookingForm, Uri.parse(webUrl))
              .takeUntil(destroyed())
              .flatMap(new Func1<BookingForm, Observable<Intent>>() {
                @Override public Observable<Intent> call(BookingForm bookingForm) {
                  return handledForm(bookingForm);
                }
              })
              .subscribe(new Action1<Intent>() {
                @Override public void call(Intent intent) {
                  publishSubjectIntent.onNext(intent);
                }
              }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                  publishSubjectIntent.onError(throwable);
                }
              });
        }
      }
      showWebView.set(false);
      return false;
    } else {
      showWebView.set(true);
      this.url.set(webUrl);
    }
    return true;
  }

  @VisibleForTesting
  Observable<Intent> handledForm(final BookingForm form) {

    return Observable.create(new Observable.OnSubscribe<Intent>() {
      @Override public void call(Subscriber<? super Intent> subscriber) {

        // if it's external action, keep loading the web view
        if (form != null && (form.isOAuthForm() || form.externalAction() != null)) {
          handleArgs(form);
        } else {
          final Intent data = new Intent();
          // Form can be null, indicating booking end (auth case)
          data.putExtra(KEY_BOOKING_FORM, (Parcelable) form);
          subscriber.onNext(data);
        }
        subscriber.onCompleted();
      }
    });
  }
}
