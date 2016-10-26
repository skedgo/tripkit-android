package com.skedgo.android.tripkit.booking;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class MyBookingsServiceImpl implements MyBookingsService {

  private final MyBookingsApi api;

  MyBookingsServiceImpl(@NonNull MyBookingsApi api) {
    this.api = api;
  }

  @Override public Observable<List<String>> getMyBookingsAsync() {
    return api.getMyBookingsAsync();
  }
}
