package com.skedgo.tripkit.regionrouting

import com.skedgo.tripkit.common.model.Location
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.regionrouting.data.GetRegionRouteRequest
import com.skedgo.tripkit.regionrouting.data.RegionRoute
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

interface RegionRoutingAutoCompleter {

    fun getRoutes(
        query: String,
        location: Location?
    ): Observable<List<RegionRoute>>

    fun getRoutes(
        regionName: String,
        query: String
    ): Single<List<RegionRoute>>

    fun sendQuery(query: AutoCompleteQuery)
    fun observe(onNext: (List<RegionRoute>) -> Unit, onError: (Throwable) -> Unit): Disposable
    fun clearDisposables()

    class RegionRoutingAutoCompleterImpl(
        private val regionRoutingApi: RegionRoutingApi,
        private val regionService: RegionService
    ) : RegionRoutingAutoCompleter {

        private val disposables = CompositeDisposable()
        private val queryAutoCompletePublishSubject = PublishSubject.create<AutoCompleteQuery>()
        private val regionRouteResult = PublishSubject.create<List<RegionRoute>>()

        init {
            disposables.add(
                queryAutoCompletePublishSubject.debounce(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        handleSearch(it)
                    }
            )
        }

        override fun getRoutes(regionName: String, query: String): Single<List<RegionRoute>> {
            val request = GetRegionRouteRequest(
                region = regionName,
                query = query
            )
            return regionRoutingApi.getRegionRoutes(
                "https://api.tripgo.com/v1/info/routes.json",
                request
            )
        }

        override fun getRoutes(query: String, location: Location?): Observable<List<RegionRoute>> {
            return (
                if (location != null) {
                    regionService.getRegionByLocationAsync(location)
                } else {
                    regionService.getRegionsAsync().map { it.first() }
                }
                ).flatMapSingle { region ->
                    val request = GetRegionRouteRequest(
                        region = region.name ?: "",
                        query = query
                    )
                    regionRoutingApi.getRegionRoutes(
                        "https://api.tripgo.com/v1/info/routes.json",
                        request
                    )
                }
        }

        override fun clearDisposables() {
            disposables.clear()
        }

        override fun observe(
            onNext: (List<RegionRoute>) -> Unit,
            onError: (Throwable) -> Unit
        ): Disposable =
            regionRouteResult
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onNext.invoke(it)
                }, {
                    onError.invoke(it)
                })

        override fun sendQuery(query: AutoCompleteQuery) {
            queryAutoCompletePublishSubject.onNext(query)
        }

        private fun handleSearch(autoCompleteQuery: AutoCompleteQuery) {
            disposables.add(
                if (autoCompleteQuery.regionName != null) {
                    getRoutes(autoCompleteQuery.regionName, autoCompleteQuery.query).toObservable()
                } else {
                    getRoutes(autoCompleteQuery.query, autoCompleteQuery.location)
                }.subscribeOn(Schedulers.io()).subscribe({
                    regionRouteResult.onNext(it)
                }, {
                    it.printStackTrace()
                })
            )
        }
    }

    class AutoCompleteQuery(
        val query: String,
        val location: Location? = null,
        val regionName: String? = null
    ) {
        private constructor(builder: Builder) : this(
            builder.query,
            builder.location,
            builder.regionName
        )

        class Builder(val query: String) {
            var location: Location? = null
            var regionName: String? = null

            fun byLocation(location: Location) =
                apply {
                    this.location = location
                    this.regionName = null
                }

            fun byRegionName(regionName: String) =
                apply {
                    this.location = null
                    this.regionName = regionName
                }

            fun build() = AutoCompleteQuery(this)
        }

    }
}