package skedgo.tripkit.a2brouting

import com.skedgo.android.tripkit.RouteService
import dagger.Module
import dagger.Provides

@Module
class A2bRoutingDomainModule {
  @Provides fun getA2bTrips(routeService: RouteService)
      = GetA2bRoutingResults(routeService)
}
