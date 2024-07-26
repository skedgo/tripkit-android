package com.skedgo.tripkit.booking.ui.view.util

import android.content.res.Resources
import android.webkit.WebView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.skedgo.tripkit.booking.ui.BookingUiComponent

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

@BindingAdapter("android:src")
fun setImageFromDrawableResource(imageView: ImageView, resourceId: Int?) {
    resourceId?.let {
        try {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, resourceId))
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }
}