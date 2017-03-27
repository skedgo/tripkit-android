package skedgo.tripkit.urlresolver.domain

import rx.Observable
import javax.inject.Inject

open class FetchDataWithUrlResolver<TData> @Inject constructor()
{
  val baseUrl = "https://tripgo.skedgo.com/satapp/"

  /** TODO: complete with the following logic:
   * https://redmine.buzzhives.com/issues/8276
   * Re-use the last region that you hit. (That would solve the problem for making a booking on matter and then asking inflationary for your bookings, which then has to go to matter for the trips that you've booked.)
  Failing that, use the region where the user is currently in. (That way inflationary doesn't be as much of a bottleneck and you're likely to get a server near you, which should be faster.)
  Failing that, hit tripgo.skedgo.com
   */
  open fun execute(fetcher: (String) -> Observable<TData>): Observable<TData> {

    return fetcher(baseUrl)

  }

}