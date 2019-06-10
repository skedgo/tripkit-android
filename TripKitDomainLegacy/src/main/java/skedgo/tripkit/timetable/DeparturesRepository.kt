package skedgo.tripkit.timetable

import com.google.gson.JsonObject
import com.skedgo.android.common.model.DeparturesResponse
import rx.Observable

interface DeparturesRepository {
  fun getTimetableEntries(region: String,
                          embarkationStopCodes: List<String>,
                          disembarkationStopCodes: List<String>?,
                          timeInSecs: Long,
                          config: JsonObject): Observable<DeparturesResponse>
}