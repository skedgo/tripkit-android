package com.skedgo.tripkit;

import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.common.model.Region;
import com.skedgo.tripkit.common.model.Regions;
import com.skedgo.tripkit.data.regions.RegionService;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public abstract class TripRegionResolver implements BiFunction<Location, Location, Observable<Region>> {
  public static TripRegionResolver create(RegionService regionService) {
    return new Impl(regionService);
  }

  private static final class Impl extends TripRegionResolver {
    private final RegionService regionService;

    private Impl(RegionService regionService) {
      this.regionService = regionService;
    }

    @Override public Observable<Region> apply(Location departure, Location arrival) {
      return Observable.combineLatest(
          regionService.getRegionByLocationAsync(departure),
          regionService.getRegionByLocationAsync(arrival),
          new BiFunction<Region, Region, Region>() {
            @Override
            public Region apply(Region departureRegion, Region arrivalRegion) {
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