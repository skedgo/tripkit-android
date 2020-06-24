package com.skedgo.tripkit.data.locations;

import io.reactivex.Observable;
import retrofit2.http.*;

import java.util.List;

public interface LocationsApi {
  /**
   * @param url A concatenation of an item of {@link Region#getURLs()} and "locations.json".
   *            For example, "https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp/locations.json".
   */
  @POST
  Observable<LocationsResponse> fetchLocationsAsync(
      @Url String url,
      @Body LocationsRequestBody body
  );

  @GET
  Observable<LocationsResponse> fetchLocationsAsync(@Url String url,
                                                    @Query("lat") double lat,
                                                    @Query("lng") double lng,
                                                    @Query("limit") int limit,
                                                    @Query("radius") int radius,
                                                    @Query("modes") List<String> modes);
}
