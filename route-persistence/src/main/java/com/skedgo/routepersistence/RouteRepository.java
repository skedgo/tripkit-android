package com.skedgo.routepersistence;

import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.skedgo.android.common.model.TripGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.functions.Func0;
import rx.subjects.PublishSubject;

public class RouteRepository {
  private final LruCache<String, CopyOnWriteArrayList<TripGroup>> memCache = new LruCache<>(1);
  private final PublishSubject<String> onNewTripGroupsAvailable = PublishSubject.create();
  private final RouteStore routeStore;
  private final Func0<Long> currentMillisProvider;

  public RouteRepository(
      RouteStore routeStore,
      Func0<Long> currentMillisProvider) {
    this.routeStore = routeStore;
    this.currentMillisProvider = currentMillisProvider;
  }

  @DebugLog public synchronized List<TripGroup> getTripGroups(String requestId) {
    final List<TripGroup> mem = memCache.get(requestId);
    if (mem != null) {
      return new ArrayList<>(mem);
    }

    final List<TripGroup> disk = routeStore.getTripGroupsByRouteIdAsync(requestId)
        .toList()
        .toBlocking()
        .first();
    memCache.put(requestId, new CopyOnWriteArrayList<>(disk));
    return disk;
  }

  public Observable<Integer> deletePastRoutesAsync() {
    return routeStore.deleteAsync(WhereClauses.happenedBefore(
        3, /* hours */
        currentMillisProvider.call()
    ));
  }

  @DebugLog public synchronized void addTripGroups(
      String requestId,
      @NonNull List<TripGroup> groups) {

    CopyOnWriteArrayList<TripGroup> existingGroups;
    existingGroups = memCache.get(requestId);
    if (existingGroups == null) {
      existingGroups = new CopyOnWriteArrayList<>();
      memCache.put(requestId, existingGroups);
    }

    existingGroups.addAll(groups);
    onNewTripGroupsAvailable.onNext(requestId);
  }

  public Observable<String> onNewTripGroupsAvailable() {
    return onNewTripGroupsAvailable.asObservable();
  }
}
