package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Trip;

import java.util.Collections;
import java.util.Map;

import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;

final class ReporterImpl implements Reporter {
  private final Func1<String, ReportingApi> apiFactory;
  private final Action1<Throwable> errorHandler;

  ReporterImpl(
      Func1<String, ReportingApi> apiFactory,
      Action1<Throwable> errorHandler) {
    this.apiFactory = apiFactory;
    this.errorHandler = errorHandler;
  }

  @Override public void reportPlannedTrip(
      @NonNull Trip trip,
      @Nullable Map<String, Object> userInfo) {
    if (trip.getPlannedURL() != null) {
      apiFactory.call(trip.getPlannedURL())
          .reportPlannedTripAsync(
              userInfo == null
                  ? Collections.<String, Object>emptyMap()
                  : userInfo
          ).subscribe(Actions.empty(), errorHandler);
    }
  }
}