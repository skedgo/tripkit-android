package skedgo.tripkit.timetable

import com.skedgo.android.common.model.DeparturesResponse
import rx.Observable

interface DeparturesRepository {
  fun getTimetableEntries(region: String,
                          embarkationStopCodes: List<String>,
                          disembarkationStopCodes: List<String>?,
                          timeInSecs: Long): Observable<DeparturesResponse>
}