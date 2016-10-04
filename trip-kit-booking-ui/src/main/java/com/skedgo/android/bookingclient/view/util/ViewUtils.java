package com.skedgo.android.bookingclient.view.util;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public final class ViewUtils {
  private ViewUtils() {}

  public static void setText(TextView view, CharSequence text) {
    if (view != null) {
      view.setText(text);
      if (TextUtils.isEmpty(text)) {
        view.setVisibility(View.GONE);
      } else {
        view.setVisibility(View.VISIBLE);
      }
    }
  }

  public static void setImage(@NonNull ImageView view, @DrawableRes int res) {
    if (res != 0) {
      view.setImageResource(res);
      view.setVisibility(View.VISIBLE);
    } else {
      view.setVisibility(View.GONE);
    }
  }

  @BindingAdapter("url")
  public static void setUrl(WebView v, String url) {
    if (url != null) {
      v.loadUrl(url);
    }
  }
}