package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.Regions;

import rx.Observable;
import rx.functions.Func2;

public abstract class TripRegionResolver implements Func2<Location, Location, Observable<Region>> {
  public static TripRegionResolver create(RegionService regionService) {
    return new Impl(regionService);
  }

  private static final class Impl extends TripRegionResolver {
    private final RegionService regionService;

    private Impl(RegionService regionService) {
      this.regionService = regionService;
    }

    @Override public Observable<Region> call(Location departure, Location arrival) {
      return Observable.combineLatest(
          regionService.getRegionByLocationAsync(departure),
          regionService.getRegionByLocationAsync(arrival),
          new Func2<Region, Region, Region>() {
            @Override
            public Region call(Region departureRegion, Region arrivalRegion) {
              if (departureRegion.equals(arrivalRegion)) {
                return departureRegion;
              } else {
                return Regions.createInterRegion(departureRegion, arrivalRegion);
              }
            }
          });
    }
  }
}