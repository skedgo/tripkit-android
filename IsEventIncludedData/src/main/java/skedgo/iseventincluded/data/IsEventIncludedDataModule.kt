package skedgo.iseventincluded.data

import android.content.Context
import dagger.Module
import dagger.Provides
import skedgo.iseventincluded.domain.IsEventIncludedRepository

@Module class IsEventIncludedDataModule {
  @Provides
  fun isEventIncludedRepository(context: Context): IsEventIncludedRepository {
    return IsEventIncludedRepositoryImpl(context.getSharedPreferences(
        "IsEventIncludedPreferences",
        Context.MODE_PRIVATE
    ))
  }
}
