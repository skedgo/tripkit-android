package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Trip;

import java.util.Collections;
import java.util.Map;

import rx.functions.Action1;
import rx.functions.Actions;

final class ReporterImpl implements Reporter {
  private final ReportingApi api;
  private final Action1<Throwable> errorHandler;

  ReporterImpl(ReportingApi api, Action1<Throwable> errorHandler) {
    this.api = api;
    this.errorHandler = errorHandler;
  }

  @Override public void reportPlannedTrip(
      @NonNull Trip trip,
      @Nullable Map<String, Object> userInfo) {
    if (trip.getPlannedURL() != null) {
      api
          .reportPlannedTripAsync(
              trip.getPlannedURL() + "/",
              userInfo == null
                  ? Collections.<String, Object>emptyMap()
                  : userInfo
          )
          .subscribe(Actions.empty(), errorHandler);
    }
  }
}
