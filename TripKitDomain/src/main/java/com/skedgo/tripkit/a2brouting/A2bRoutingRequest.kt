package com.skedgo.tripkit.a2brouting

import org.immutables.value.Value
import org.immutables.value.Value.Style.ImplementationVisibility

@Value.Immutable
@Value.Style(visibility = ImplementationVisibility.PACKAGE)
abstract class A2bRoutingRequest {
  companion object {
    fun builder(): Builder = ImmutableA2bRoutingRequest.builder()
  }

  abstract val origin: Pair<Double, Double>
  abstract val destination: Pair<Double, Double>
  abstract val originAddress: String?
  abstract val destinationAddress: String?
  abstract val time: RequestTime

  interface Builder {
    fun origin(origin: Pair<Double, Double>): Builder
    fun destination(destination: Pair<Double, Double>): Builder
    fun originAddress(originAddress: String?): Builder
    fun destinationAddress(destinationAddress: String?): Builder
    fun time(time: RequestTime): Builder
    fun build(): A2bRoutingRequest
  }
}
