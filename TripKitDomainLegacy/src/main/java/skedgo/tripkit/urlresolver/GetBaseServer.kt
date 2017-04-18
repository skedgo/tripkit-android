package skedgo.tripkit.urlresolver

import rx.Observable
import javax.inject.Inject

open class GetBaseServer @Inject constructor() {
  val baseUrl = "https://tripgo.skedgo.com/satapp"

  open fun execute(): Observable<String> {
    return  Observable.just(baseUrl)
  }

}

