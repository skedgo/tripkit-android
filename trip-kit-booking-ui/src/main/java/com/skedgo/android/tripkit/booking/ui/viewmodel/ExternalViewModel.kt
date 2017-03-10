package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.webkit.WebView
import android.webkit.WebViewClient
import com.skedgo.android.tripkit.booking.ExternalFormField
import rx.Observable
import rx.subjects.PublishSubject
import javax.inject.Inject

class ExternalViewModel @Inject internal constructor() : DisposableViewModel() {

  private val publishSubjectNextUrl = PublishSubject.create<String>()
  private var externalFormField: ExternalFormField? = null

  val showWebView: ObservableBoolean = ObservableBoolean(false)
  val url: ObservableField<String> = ObservableField()
  val nextUrlObservable: Observable<String> = publishSubjectNextUrl.asObservable()


  fun handleArgs(args: Bundle) {
    externalFormField = args.getParcelable<ExternalFormField>("externalFormField")
    externalFormField?.let { url.set(it.value) }
  }

  fun webViewClient(): WebViewClient {
    return object : WebViewClient() {
      override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
        return handleCallback(url)
      }

      override fun onPageFinished(view: WebView, url: String) {
        with(externalFormField!!) {
          if (url.startsWith(disregardURL) && value!!.startsWith(disregardURL)) {
            publishSubjectNextUrl.onNext(nextURL)
          } else {
            showWebView.set(true)
          }
        }
      }
    }
  }

  @VisibleForTesting
  internal fun handleCallback(webUrl: String): Boolean {
    return when {
      webUrl.startsWith(externalFormField!!.disregardURL) -> {
        publishSubjectNextUrl.onNext(externalFormField!!.nextURL)
        false
      }
      else -> {
        showWebView.set(true)
        url.set(webUrl)
        true
      }
    }
  }
}