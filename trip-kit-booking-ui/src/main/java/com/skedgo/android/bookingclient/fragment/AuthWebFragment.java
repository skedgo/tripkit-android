package com.skedgo.android.bookingclient.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.bookingclient.OAuth2CallbackHandler;
import com.skedgo.android.bookingclient.activity.BookingActivity;
import com.skedgo.android.bookingclient.module.BookingClientComponent;
import com.skedgo.android.bookingclient.module.BookingClientModule;
import com.skedgo.android.bookingclient.module.DaggerBookingClientComponent;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.FormField;
import com.skedgo.android.tripkit.booking.FormFieldJsonAdapter;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import skedgo.common.view.ButterKnifeFragment;

public class AuthWebFragment extends ButterKnifeFragment {

  private WebView webView;

  public static AuthWebFragment newInstance(String url) {
    final Bundle args = new Bundle();

    args.putString("url", url);

    final AuthWebFragment fragment = new AuthWebFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    if (webView != null) {
      webView.destroy();
    }
    webView = new WebView(getActivity());
    return webView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    String url = getArguments().getString("url");

    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setLoadWithOverviewMode(true);
    webView.getSettings().setUseWideViewPort(true);
    webView.setWebViewClient(new WebViewClient() {

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith("tripgo://oauth-callback")) {

          final BookingClientComponent bookingClientComponent = DaggerBookingClientComponent.builder()
              .bookingClientModule(new BookingClientModule(getActivity()))
              .build();

          OAuth2CallbackHandler oAuth2CallbackHandler = bookingClientComponent.getOAuth2CallbackHandler();

          String callback = url.substring(0, url.indexOf("?"));

          oAuth2CallbackHandler.handleURL(getActivity(), Uri.parse(url), callback)
              .timeout(40, TimeUnit.SECONDS, Schedulers.newThread()).takeUntil(lifecycle().onDestroy())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.newThread())
              .subscribe(new Action1<BookingForm>() {
                @Override public void call(BookingForm form) {
                  final Intent data = new Intent().putExtra(BookingActivity.KEY_FORM, (Parcelable) form);
                  getActivity().setResult(Activity.RESULT_OK, data);
                  getActivity().finish();
                }
              }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                  // handle error
                }
              });
          return false;
        } else if (url.startsWith("tripgo://booking_retry")) {

          SharedPreferences prefs = getActivity().getSharedPreferences(BookingActivity.KEY_TEMP_BOOKING, Activity.MODE_PRIVATE);
          String jsonForm = prefs.getString(BookingActivity.KEY_TEMP_BOOKING_FORM, null);
          if (jsonForm != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(FormField.class, new FormFieldJsonAdapter());
            Gson gson = builder.create();

            BookingForm form = gson.fromJson(jsonForm, BookingForm.class);

            final Intent data = new Intent().putExtra(BookingActivity.KEY_FORM, (Parcelable) form);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
          }
          return false;

        } else {
          view.loadUrl(url);

          return true;
        }

      }

      @Override
      public void onPageFinished(WebView view, final String url) {

      }
    });

    webView.loadUrl(url);
  }

}
