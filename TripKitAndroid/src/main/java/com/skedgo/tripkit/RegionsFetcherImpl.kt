package com.skedgo.tripkit

import android.database.sqlite.SQLiteOpenHelper
import com.skedgo.tripkit.common.model.RegionsResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function

internal class RegionsFetcherImpl(
        private val api: RegionsApi,
        private val databaseHelper: SQLiteOpenHelper
) : RegionsFetcher {

    override fun fetchAsync(): Completable {

        resetRegionsTable()

        return api.fetchRegionsAsync(RegionsApi.RequestBodyContent(2, null))
                .flatMap(saveToDiskCache()).ignoreElements()
    }

    private fun resetRegionsTable() {
        try {
            databaseHelper.writableDatabase.beginTransaction()

            databaseHelper.writableDatabase.delete(Tables.REGIONS.name, null, null)
            databaseHelper.writableDatabase.delete(Tables.TRANSPORT_MODES.name, null, null)

            databaseHelper.writableDatabase.setTransactionSuccessful()
        } catch (e: Exception) {}
        finally {
            databaseHelper.writableDatabase.endTransaction()
        }
    }

    private fun saveToDiskCache(): Function<RegionsResponse, Observable<Void>> =
            Function { response ->
                Observable.create(OnSubscribeSaveRegionsResponse(
                        databaseHelper.writableDatabase,
                        response
                ))
            }
}
