package com.skedgo.android.tripkit.booking.ui.view.util

import androidx.databinding.BindingAdapter
import android.webkit.WebView
import android.widget.ImageView
import com.skedgo.android.tripkit.booking.ui.BookingUiComponent

@BindingAdapter("bookingImageUrl")
fun loadImageUrl(component: BookingUiComponent, view: ImageView, imageUrl: String?) {
  if (imageUrl == null) {
    view.setImageBitmap(null)
  } else {
    component.picasso()
        .load(imageUrl)
        .into(view)
  }
}

@BindingAdapter("url")
fun setUrl(v: WebView, url: String?) {
  if (url != null) {
    v.loadUrl(url)
  }
}