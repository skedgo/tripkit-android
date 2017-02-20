package com.skedgo.android.tripkit.booking.ui.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.skedgo.android.tripkit.booking.ExternalFormField;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;
import skedgo.common.BaseViewModel;

public class ExternalViewModel extends BaseViewModel {

  private final ObservableBoolean showWebView;
  private final ObservableField<String> url;
  private final PublishSubject<String> publishSubjectNextUrl = PublishSubject.create();
  private ExternalFormField externalFormField;

  @Inject ExternalViewModel() {
    showWebView = new ObservableBoolean(false);
    url = new ObservableField<>();
  }

  public ObservableBoolean showWebView() {
    return showWebView;
  }

  public ObservableField<String> url() {
    return url;
  }

  public Observable<String> nextUrlObservable() {
    return publishSubjectNextUrl.asObservable();
  }

  public void handleArgs(Bundle args) {
    externalFormField = args.getParcelable("externalFormField");
    if (externalFormField != null) {
      url.set(externalFormField.getValue());
    }
  }

  public WebViewClient webViewClient() {
    return new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return handleCallback(url);
      }

      @Override
      public void onPageFinished(WebView view, final String url) {
        if (url.startsWith(externalFormField.getDisregardURL()) &&
            externalFormField.getValue().startsWith(externalFormField.getDisregardURL())) {
          publishSubjectNextUrl.onNext(externalFormField.getNextURL());
        } else {
          showWebView.set(true);
        }
      }
    };
  }

  @VisibleForTesting
  boolean handleCallback(String webUrl) {
    if (webUrl.startsWith(externalFormField.getDisregardURL())) {
      publishSubjectNextUrl.onNext(externalFormField.getNextURL());
      return false;
    } else {
      showWebView.set(true);
      this.url.set(webUrl);
    }
    return true;
  }

}
