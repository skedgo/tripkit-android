package com.skedgo.android.tripkit.booking.ui.view.util

import android.databinding.BindingAdapter
import android.webkit.WebView
import android.widget.ImageView
import com.skedgo.android.tripkit.booking.ui.BookingUiComponent


@BindingAdapter("imageUrl")
fun loadImageUrl(component: BookingUiComponent, view: ImageView, imageUrl: String) {
  component.picasso()
      .load(imageUrl)
      .into(view)
}

@BindingAdapter("url")
fun setUrl(v: WebView, url: String?) {
  if (url != null) {
    v.loadUrl(url)
  }
}


