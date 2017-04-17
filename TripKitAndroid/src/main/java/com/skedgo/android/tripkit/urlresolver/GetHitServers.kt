package com.skedgo.android.tripkit.urlresolver

import rx.Observable
import javax.inject.Inject

open class GetHitServers @Inject constructor(
    private val getBaseServer: GetBaseServer,
    private val getLastUsedRegion: GetLastUsedRegion
) {

  open fun execute(): Observable<String> {
    return getLastUsedRegion.execute()
        .map { region -> region.urLs }
        .flatMap { urls -> Observable.from<String>(urls) }
        .onErrorResumeNext { getBaseServer.execute() }
        .concatWith(getBaseServer.execute())

  }

}