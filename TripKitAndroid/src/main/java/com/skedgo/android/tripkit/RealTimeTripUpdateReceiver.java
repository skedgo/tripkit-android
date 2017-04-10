package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.TripGroup;

import rx.Observable;

public interface RealTimeTripUpdateReceiver {
  Observable<TripGroup> startAsync();
  void stop();
}