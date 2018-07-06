package skedgo.tripkit.android

import dagger.Subcomponent
import skedgo.tripkit.a2brouting.GetTripLine

/**
 * Creates UseCases and Repositories related to the A2bRouting feature.
 */
@FeatureScope
@Subcomponent
interface A2bRoutingComponent {
  val getTripLine: GetTripLine
}