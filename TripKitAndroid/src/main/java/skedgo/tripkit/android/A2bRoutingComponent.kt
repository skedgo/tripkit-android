package skedgo.tripkit.android

import dagger.Subcomponent
import skedgo.tripkit.a2brouting.GetA2bRoutingResults
import skedgo.tripkit.a2brouting.GetTripLine
import javax.inject.Singleton

/**
 * Creates UseCases and Repositories related to the A2bRouting feature.
 */
@Singleton
@Subcomponent
interface A2bRoutingComponent {
  fun getA2bRoutingResults(): GetA2bRoutingResults
  fun getTripLine(): GetTripLine
}