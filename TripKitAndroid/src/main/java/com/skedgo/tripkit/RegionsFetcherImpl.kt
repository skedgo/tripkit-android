package com.skedgo.tripkit

import android.database.sqlite.SQLiteOpenHelper

import com.skedgo.tripkit.common.model.RegionsResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function

internal class RegionsFetcherImpl(
        private val api: com.skedgo.tripkit.RegionsApi,
        private val databaseHelper: SQLiteOpenHelper
) : RegionsFetcher {
  override fun fetchAsync(): Completable =
      api.fetchRegionsAsync(com.skedgo.tripkit.RegionsApi.RequestBodyContent(2, null))
          .flatMap(saveToDiskCache()).ignoreElements()

  private fun saveToDiskCache(): Function<RegionsResponse, Observable<Void>> =
      Function { response ->
        Observable.create(com.skedgo.tripkit.OnSubscribeSaveRegionsResponse(
                databaseHelper.writableDatabase,
                response
        ))
      }
}
