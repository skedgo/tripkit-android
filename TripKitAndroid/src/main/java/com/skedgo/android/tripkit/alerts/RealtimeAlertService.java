package com.skedgo.android.tripkit.alerts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Region;

import java.util.List;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

public class RealtimeAlertService {
  private final RealtimeAlertApi api;

  @Inject
  public RealtimeAlertService(@NonNull RealtimeAlertApi api) {
    this.api = api;
  }

  /**
   * @param baseUrls   Which can be obtained via {@link Region#getURLs()}.
   * @param regionName Which can be obtained via {@link Region#getName()}.
   */
  public Observable<RealtimeAlertResponse> fetchRealtimeAlertsAsync(
      @Nullable List<String> baseUrls,
      @Query("region") final String regionName) {
    if (baseUrls == null) {
      return Observable.error(new NullPointerException("baseUrls is null"));
    } else {
      return Observable.from(baseUrls)
          .concatMapDelayError(new Func1<String, Observable<? extends RealtimeAlertResponse>>() {
            @Override public Observable<? extends RealtimeAlertResponse> call(String baseUrl) {
              final String url = HttpUrl.parse(baseUrl)
                  .newBuilder()
                  .addPathSegments("alerts/transit.json")
                  .build()
                  .toString();
              return api.fetchRealtimeAlertsAsync(url, regionName);
            }
          })
          .first();
    }
  }
}