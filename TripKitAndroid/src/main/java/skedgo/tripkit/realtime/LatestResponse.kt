package skedgo.tripkit.realtime

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersLatestResponse::class)
interface LatestResponse {
  fun services(): Array<LatestServiceResponse>
}