package skedgo.tripkit.routing

import org.immutables.gson.Gson
import org.immutables.value.Value

@Gson.TypeAdapters
@Value.Immutable
interface Provider {
  fun name(): String
}
