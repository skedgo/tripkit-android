package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

final class QueryGeneratorImpl implements Func2<Query, ModeFilter, Observable<List<Query>>> {
  private final RegionService regionService;
  private final ModeCombinationStrategy modeCombinationStrategy = new ModeCombinationStrategy();
  private final TripRegionResolver tripRegionResolver;

  QueryGeneratorImpl(@NonNull RegionService regionService) {
    this.regionService = regionService;
    tripRegionResolver = TripRegionResolver.create(regionService);
  }

  @Override
  public Observable<List<Query>> call(@NonNull final Query query, @NonNull final ModeFilter modeFilter) {
    final Location departure = query.getFromLocation();
    if (departure == null) {
      return Observable.error(new NullPointerException("Departure is null"));
    }

    final Location arrival = query.getToLocation();
    if (arrival == null) {
      return Observable.error(new NullPointerException("Arrival is null"));
    }

    return tripRegionResolver.call(departure, arrival)
        .flatMap(new Func1<Region, Observable<Map<String, TransportMode>>>() {
          @Override
          public Observable<Map<String, TransportMode>> call(Region region) {
            query.setRegion(region);
            final List<String> modeIds = modeFilter.execute(region);
            if (modeIds != null) {
              query.setTransportModeIds(new ArrayList<>(modeIds));
            }
            return regionService.getTransportModesAsync();
          }
        })
        .map(new Func1<Map<String, TransportMode>, List<Query>>() {
          @Override
          public List<Query> call(Map<String, TransportMode> modeMap) {
            final List<Set<String>> modeSets = modeCombinationStrategy.call(
                modeMap,
                query.getTransportModeIds()
            );

            final List<Query> queries = new ArrayList<>(modeSets.size());
            for (Set<String> modeSet : modeSets) {
              Query clone = query.clone();
              clone.getTransportModeIds().addAll(modeSet);
              queries.add(clone);
            }

            return queries;
          }
        });
  }
}