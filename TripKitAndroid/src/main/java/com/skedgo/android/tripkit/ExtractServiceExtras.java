package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.skedgo.android.common.model.ServiceStop;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

class ExtractServiceExtras implements Func1<List<ServiceStop>, ServiceExtras> {
  private final String startStopCode;
  private final String endStopCode;

  private ExtractServiceExtras(String startStopCode, String endStopCode) {
    this.startStopCode = startStopCode;
    this.endStopCode = endStopCode;
  }

  @NonNull
  public static ExtractServiceExtras create(String startStopCode, String endStopCode) {
    return new ExtractServiceExtras(startStopCode, endStopCode);
  }

  @Override
  public ServiceExtras call(List<ServiceStop> stops) {
    if (CollectionUtils.isEmpty(stops)) {
      return new ServiceExtras(null, null);
    }

    boolean isStartStopFound = false;
    String platform = null;
    final List<ServiceStop> tripStops = new ArrayList<>(stops.size());
    for (ServiceStop stop : stops) {
      String stopCode = stop.getCode();
      if (!isStartStopFound) {
        if (TextUtils.equals(stopCode, startStopCode)) {
          // Start stop found!
          tripStops.add(stop);
          platform = stop.getName();

          // Mark that the start stop was found.
          isStartStopFound = true;
        }
      } else {
        if (TextUtils.equals(stopCode, endStopCode)) {
          // End stop found!
          tripStops.add(stop);

          // Stop iterating since the end stop was found.
          break;
        } else {
          // On the way to the end stop.
          tripStops.add(stop);
        }
      }
    }

    return new ServiceExtras(platform, tripStops);
  }
}