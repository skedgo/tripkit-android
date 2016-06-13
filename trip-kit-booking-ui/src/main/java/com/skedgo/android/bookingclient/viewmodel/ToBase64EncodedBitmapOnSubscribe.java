package com.skedgo.android.bookingclient.viewmodel;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

public class ToBase64EncodedBitmapOnSubscribe implements Observable.OnSubscribe<String> {
  private final Bitmap bitmap;

  public ToBase64EncodedBitmapOnSubscribe(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  @Override
  public void call(Subscriber<? super String> subscriber) {
    if (bitmap == null) {
      subscriber.onError(new NullPointerException("Bitmap is null"));
      return;
    }

    final ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
    final byte[] bitmapByteArray = bitmapStream.toByteArray();
    final String encoded = Base64.encodeToString(bitmapByteArray, 0);

    try {
      bitmapStream.close();
    } catch (IOException e) {
      // Should we propagate this error?
    } finally {
      bitmap.recycle();
    }

    subscriber.onNext(encoded);
    subscriber.onCompleted();
  }
}