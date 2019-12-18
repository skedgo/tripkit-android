package com.skedgo.tripkit.data.locations;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

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
}
