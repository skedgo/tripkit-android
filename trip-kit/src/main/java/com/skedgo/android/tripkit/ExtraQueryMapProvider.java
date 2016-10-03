package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.tripkit.routing.RoutingApi;

import java.util.Map;

/**
 * A decorator that puts additional query params
 * into the query map that is supplied into {@link RoutingApi}.
 * Note that you should only use this when
 * you really do know what you intend to do.
 */
public interface ExtraQueryMapProvider {
  /**
   * Be careful that some entries of this map
   * may override some default entries of
   * the query map of {@link RoutingApi}.
   */
  @NonNull Map<String, Object> call();
}