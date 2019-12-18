package com.skedgo.tripkit.booking.ui.viewmodel

import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import android.webkit.WebView
import android.webkit.WebViewClient
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.ui.OAuth2CallbackHandler
import com.skedgo.tripkit.booking.ui.activity.KEY_FORM
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ExternalProviderAuthViewModel @Inject internal constructor() : DisposableViewModel() {

  private val publishSubjectIntent = PublishSubject.create<Intent>()
  val url: ObservableField<String> = ObservableField()
  val showWebView: ObservableBoolean = ObservableBoolean(false)
  val intentObservable: Observable<Intent> = publishSubjectIntent.hide()

  @VisibleForTesting var bookingForm: BookingForm? = null

  fun handleArgs(args: Bundle) {
    val form = args.getParcelable<BookingForm>(KEY_FORM)
    handleArgs(form)
  }

  fun webViewClient(oAuth2CallbackHandler: OAuth2CallbackHandler): WebViewClient {
    return object : WebViewClient() {
      override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
        return handleCallback(url, oAuth2CallbackHandler)
      }

      override fun onPageFinished(view: WebView, url: String) {
        showWebView.set((!url.startsWith("tripgo://")))
      }
    }
  }

  @VisibleForTesting
  fun handleCallback(webUrl: String, oAuth2CallbackHandler: OAuth2CallbackHandler): Boolean {

    with(webUrl) {
      return when {
        startsWith("tripgo://") -> {
          bookingForm?.let {
            when {
              startsWith("tripgo://oauth-callback") -> {
                val callback = substring(0, indexOf("?"))
                oAuth2CallbackHandler.handleOAuthURL(bookingForm!!, Uri.parse(this), callback)
              }
              startsWith("tripgo://booking_retry") -> oAuth2CallbackHandler.handleRetryURL(bookingForm!!, Uri.parse(this))
              else -> null
            }
                ?.takeUntil(onDispose())
                ?.flatMap { bookingForm -> handledForm(bookingForm) }
                ?.subscribe({ intent -> publishSubjectIntent.onNext(intent) }) { throwable ->
                  publishSubjectIntent.onError(throwable)
                }
          }
          showWebView.set(false)
          false
        }
        else -> {
          showWebView.set(true)
          url.set(this)
          true
        }
      }
    }

  }

  @VisibleForTesting
  fun handledForm(form: BookingForm?): Observable<Intent> {

    return Observable.create { subscriber ->
      when {
      // if it's external action, keep loading the web view
        form?.isOAuthForm ?: false || form?.externalAction() != null -> handleArgs(form!!)
        else -> {
          val data = Intent()
          // Form can be null, indicating booking end (auth case).
          data.putExtra(KEY_FORM, form as Parcelable?)
          subscriber.onNext(data)
        }
      }
      subscriber.onComplete()
    }
  }

  private fun handleArgs(form: BookingForm?) {
    this.bookingForm = form
    bookingForm?.let {
      when {
        it.isOAuthForm && it.oAuthLink != null -> url.set(it.oAuthLink.toString())
        it.externalAction() != null -> url.set(it.externalAction())
      }
    }
  }
}