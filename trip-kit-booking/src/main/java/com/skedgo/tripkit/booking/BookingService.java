package com.skedgo.tripkit.booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Flowable;

public interface BookingService {
  Flowable<BookingForm> getFormAsync(@NonNull String url);
  Flowable<BookingForm> postFormAsync(@NonNull String url, @Nullable InputForm inputForm);
  Flowable<BookingForm> postActionInputAsync(@NonNull String url, @NonNull String field, @NonNull String value);
}
