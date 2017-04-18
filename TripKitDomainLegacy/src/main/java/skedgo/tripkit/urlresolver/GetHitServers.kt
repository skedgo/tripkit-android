package skedgo.tripkit.urlresolver

import rx.Observable
import javax.inject.Inject

open class GetHitServers @Inject constructor(
    private val getBaseServer: GetBaseServer,
    private val getLastUsedRegionUrls: GetLastUsedRegionUrls
) {

  open fun execute(): Observable<String> {
    return getLastUsedRegionUrls.getLastUsedRegionUrls()
        .flatMap { urls -> Observable.from<String>(urls) }
        .concatWith(getBaseServer.execute())
        .onErrorResumeNext { getBaseServer.execute() }

  }

}