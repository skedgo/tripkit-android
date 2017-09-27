package skedgo.tripkit.a2brouting;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.tripkit.RoutingUserError;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import rx.Observable;
import rx.functions.Func1;
import skedgo.tripkit.routing.RoutingResponse;
import skedgo.tripkit.routing.TripGroup;

/**
 * A wrapper of {@link A2bRoutingApi} that requests `routing.json`
 * on multiple servers w/ failover.
 */
public class FailoverA2bRoutingApi {
  private final SelectBestDisplayTrip selectBestDisplayTrip = new SelectBestDisplayTrip();
  private final FillIdentifiers fillIdentifiers = new FillIdentifiers();
  private final Resources resources;
  private final Gson gson;
  private final A2bRoutingApi a2bRoutingApi;

  public FailoverA2bRoutingApi(
      Resources resources,
      Gson gson,
      A2bRoutingApi a2bRoutingApi
  ) {
    this.resources = resources;
    this.gson = gson;
    this.a2bRoutingApi = a2bRoutingApi;
  }

  /**
   * Fetches routes on multiple base urls serially.
   * If it fails on one url, it'll failover on next url.
   *
   * @param baseUrls Can be obtained by {@link Region#getURLs()}.
   */
  public Observable<List<TripGroup>> fetchRoutesAsync(
      List<String> baseUrls,
      final List<String> modes,
      final List<String> excludedTransitModes,
      final Map<String, Object> options
  ) {
    // Regarding url failover, see more
    // https://www.flowdock.com/app/skedgo/tripgo-v4/threads/ZSuBe4bGCfR8ltaEosihtCklBZy.
    return Observable.from(baseUrls)
        .map(new Func1<String, String>() {
          @Override public String call(String baseUrl) {
            return HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("routing.json")
                .build()
                .toString();
          }
        })
        .concatMap(new Func1<String, Observable<RoutingResponse>>() {
          @Override public Observable<RoutingResponse> call(String url) {
            return fetchRoutesPerUrlAsync(url, modes, excludedTransitModes, options);
          }
        })
        .first()
        .map(new Func1<RoutingResponse, List<TripGroup>>() {
          @Override public List<TripGroup> call(RoutingResponse response) {
            response.processRawData(resources, gson);
            return response.getTripGroupList();
          }
        })
        .filter(new Func1<List<TripGroup>, Boolean>() {
          @Override public Boolean call(List<TripGroup> groups) {
            return CollectionUtils.isNotEmpty(groups);
          }
        })
        .map(fillIdentifiers)
        .map(new Func1<List<TripGroup>, List<TripGroup>>() {
          @Override public List<TripGroup> call(List<TripGroup> groups) {
            for (TripGroup group : groups) {
              selectBestDisplayTrip.call(group);
            }
            return groups;
          }
        })
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<TripGroup>>>() {
          @Override public Observable<? extends List<TripGroup>> call(Throwable error) {
            return error instanceof RoutingUserError
                ? Observable.<List<TripGroup>>error(error)
                : Observable.<List<TripGroup>>empty();
          }
        });
  }

  Observable<RoutingResponse> fetchRoutesPerUrlAsync(
      final String url,
      final List<String> modes,
      final List<String> excludedTransitModes,
      final Map<String, Object> options
  ) {
    return a2bRoutingApi
        .execute(url, modes, excludedTransitModes, options)
        .filter(new Func1<RoutingResponse, Boolean>() {
          @Override public Boolean call(RoutingResponse response) {
            return !(response.getErrorMessage() != null && !response.hasError());
          }
        })
        /* Let it fail silently. */
        .onErrorResumeNext(Observable.<RoutingResponse>empty())
        .flatMap(new Func1<RoutingResponse, Observable<RoutingResponse>>() {
          @Override public Observable<RoutingResponse> call(RoutingResponse response) {
            if (response.getErrorMessage() != null) {
              return Observable.error(new RoutingUserError(response.getErrorMessage()));
            } else {
              return Observable.just(response);
            }
          }
        });
  }
}