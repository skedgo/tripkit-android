package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.ServiceStop;
import com.skedgo.android.common.model.Shape;
import com.skedgo.android.common.model.TripSegment;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

class AlphaServiceExtrasService implements ServiceExtrasService {
  private final RegionService regionService;
  private final DynamicEndpoint serviceApiEndpoint;
  private final ServiceApi serviceApi;

  AlphaServiceExtrasService(@NonNull RegionService regionService,
                            @NonNull DynamicEndpoint serviceApiEndpoint,
                            @NonNull ServiceApi serviceApi) {
    this.regionService = regionService;
    this.serviceApiEndpoint = serviceApiEndpoint;
    this.serviceApi = serviceApi;
  }

  @NonNull
  @Override
  public Observable<ServiceExtras> getServiceExtrasAsync(@NonNull final TripSegment segment) {
    return regionService.getRegionByLocationAsync(segment.getFrom())
        .flatMap(new Func1<Region, Observable<ServiceResponse>>() {
          @Override
          public Observable<ServiceResponse> call(Region region) {
            serviceApiEndpoint.setUrl(region.getURLs().get(0));
            return serviceApi.getServiceAsync(
                region.getName(),
                segment.getServiceTripId(),
                segment.getStartTimeInSecs(),
                true
            );
          }
        })
        .flatMap(new Func1<ServiceResponse, Observable<Shape>>() {
          @Override
          public Observable<Shape> call(ServiceResponse response) {
            return Observable.from(response.getShapes());
          }
        })
        .first()
        .map(new Func1<Shape, List<ServiceStop>>() {
          @Override
          public List<ServiceStop> call(Shape shape) {
            return shape.getStops();
          }
        })
        .map(ExtractServiceExtras.create(
            segment.getStartStopCode(),
            segment.getEndStopCode()
        ));
  }
}