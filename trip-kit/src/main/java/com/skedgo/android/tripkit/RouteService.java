package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.TripGroup;

import java.util.List;

import rx.Observable;

public interface RouteService {
  @NonNull Observable<List<TripGroup>> routeAsync(@NonNull Query query);
}
