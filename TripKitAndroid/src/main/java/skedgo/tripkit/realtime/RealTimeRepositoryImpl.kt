package skedgo.tripkit.realtime

import com.skedgo.android.common.agenda.IRealTimeElement
import com.skedgo.android.tripkit.RegionService
import okhttp3.HttpUrl
import rx.Observable
import skedgo.tripkit.routing.RealTimeVehicle
import javax.inject.Inject

private const val LATEST_ENDPOINT = "latest.json"

class RealTimeRepositoryImpl @Inject constructor(
    private val latestApi: LatestApi,
    private val regionService: RegionService
) : RealTimeRepository {

  override fun getUpdates(region: String, elements: List<IRealTimeElement>): Observable<List<RealTimeVehicle>> =
      regionService.getRegionByNameAsync(region)
          .flatMap { Observable.from(it.urLs) }
          .map {
            HttpUrl.parse(it)!!
                .newBuilder()
                .addPathSegment(LATEST_ENDPOINT)
                .build()
                .toString()
          }
          .concatMap { url ->
            val latestServices = elements.map {
              ImmutableLatestService.builder()
                  .operator(it.operator)
                  .serviceTripID(it.serviceTripId)
                  .startStopCode(it.startStopCode)
                  .endStopCode(it.endStopCode)
                  .startTime(it.startTimeInSecs)
                  .build()
            }

            val requestBody = ImmutableLatestRequestBody.builder()
                .region(region)
                .services(latestServices)
                .build()

            latestApi.request(url, requestBody)
                .toObservable()
                .onErrorResumeNext(Observable.empty())
          }
          .map { latestResponse ->
            latestResponse.services().map {
              it.toRealTimeVehicle()
            }
          }
          .first()
}