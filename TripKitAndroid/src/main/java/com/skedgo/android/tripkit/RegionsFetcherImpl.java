package com.skedgo.android.tripkit;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.skedgo.android.common.model.RegionsResponse;

import rx.Observable;
import rx.functions.Func1;

final class RegionsFetcherImpl implements RegionsFetcher {
  private final RegionsApi api;
  private final SQLiteOpenHelper databaseHelper;

  RegionsFetcherImpl(RegionsApi api, SQLiteOpenHelper databaseHelper) {
    this.api = api;
    this.databaseHelper = databaseHelper;
  }

  @Override public Observable<Void> fetchAsync() {
    return api
        .fetchRegionsAsync(new RegionsApi.RequestBodyContent(2, null))
        .flatMap(saveToDiskCache());
  }

  @NonNull private Func1<RegionsResponse, Observable<Void>> saveToDiskCache() {
    return new Func1<RegionsResponse, Observable<Void>>() {
      @Override
      public Observable<Void> call(RegionsResponse response) {
        return Observable.create(new OnSubscribeSaveRegionsResponse(
            databaseHelper.getWritableDatabase(),
            response
        ));
      }
    };
  }
}