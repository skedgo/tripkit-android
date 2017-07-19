package com.skedgo.android.tripkit

import android.database.sqlite.SQLiteOpenHelper

import com.skedgo.android.common.model.RegionsResponse

import rx.Observable
import rx.functions.Func1

internal class RegionsFetcherImpl(
    private val api: RegionsApi,
    private val databaseHelper: SQLiteOpenHelper
) : RegionsFetcher {
  override fun fetchAsync(): Observable<Void> =
      api
          .fetchRegionsAsync(RegionsApi.RequestBodyContent(2, null))
          .flatMap(saveToDiskCache())

  private fun saveToDiskCache(): Func1<RegionsResponse, Observable<Void>> =
      Func1 { response ->
        Observable.create(OnSubscribeSaveRegionsResponse(
            databaseHelper.writableDatabase,
            response
        ))
      }
}
