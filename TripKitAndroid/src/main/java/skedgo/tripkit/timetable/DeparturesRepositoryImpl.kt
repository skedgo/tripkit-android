package skedgo.tripkit.timetable

import android.os.Parcelable
import com.google.gson.JsonObject
import com.skedgo.android.common.model.DeparturesResponse
import com.skedgo.android.tripkit.RegionService
import okhttp3.HttpUrl
import org.json.JSONObject
import rx.Observable
import javax.inject.Inject

private const val DEPARTURES_ENDPOINT = "departures.json"

class DeparturesRepositoryImpl @Inject constructor(
    private val departuresApi: DeparturesApi,
    private val regionService: RegionService
) : DeparturesRepository {

  override fun getTimetableEntries(
      region: String,
      embarkationStopCodes: List<String>,
      disembarkationStopCodes: List<String>?,
      timeInSecs: Long
  ): Observable<DeparturesResponse> =
      regionService.getRegionByNameAsync(region)
          .flatMap { Observable.from(it.urLs) }
          .map {
            HttpUrl.parse(it)!!
                .newBuilder()
                .addPathSegment(DEPARTURES_ENDPOINT)
                .build()
                .toString()
          }
          .concatMap { url ->
            val requestBody = ImmutableDepartureRequestBody.builder()
                .embarkationStops(embarkationStopCodes)
                .disembarkationStops(disembarkationStopCodes)
                .regionName(region)
                .timeInSecs(timeInSecs)
                .build()

            departuresApi.request(url, requestBody)
                .toObservable()
                .onErrorResumeNext(Observable.empty())
          }
          .first()
          .map { it.postProcess(embarkationStopCodes, disembarkationStopCodes) }
}