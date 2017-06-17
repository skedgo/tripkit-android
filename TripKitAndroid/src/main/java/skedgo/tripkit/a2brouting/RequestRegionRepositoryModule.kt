package skedgo.tripkit.a2brouting

import android.content.Context
import com.skedgo.android.tripkit.RegionService
import dagger.Module
import dagger.Provides

@Module class RequestRegionRepositoryModule {
  @Provides
  fun requestRegionRepository(
      context: Context,
      regionService: RegionService
  ): RequestRegionRepository =
      RequestRegionRepositoryImpl(
          context.getSharedPreferences(
              "RequestRegionPrefs", Context.MODE_PRIVATE
          ),
          regionService
      )
}
