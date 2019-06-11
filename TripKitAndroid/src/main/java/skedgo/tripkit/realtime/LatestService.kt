package skedgo.tripkit.realtime

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersLatestService::class)
interface LatestService {
  fun operator(): String
  fun serviceTripID(): String
  fun startStopCode(): String
  fun startTime(): Long
  fun endStopCode(): String?
}