package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import java.util.List;

import rx.functions.Func1;

public interface ExcludedTransitModesAdapter extends Func1<String, List<String>> {
  /**
   * @return A list of transit modes that users wish to
   * avoid in results related to public transports.
   */
  @Nullable List<String> call(@Nullable String regionName);
}