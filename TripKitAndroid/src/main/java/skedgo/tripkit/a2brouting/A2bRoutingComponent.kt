package skedgo.tripkit.a2brouting

import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent
interface A2bRoutingComponent {
  fun getA2bRoutingResults(): GetA2bRoutingResults
  fun getTripLine(): GetTripLine
}