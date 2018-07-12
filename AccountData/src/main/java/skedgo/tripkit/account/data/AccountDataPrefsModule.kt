package skedgo.tripkit.account.data

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AccountDataPrefsModule {
  companion object {
    const val AccountDataPrefs = "AccountDataPrefs"
  }

  @Provides
  @Named(AccountDataPrefs)
  fun accountDataPrefs(context: Context): SharedPreferences =
      context.getSharedPreferences(AccountDataPrefs, Context.MODE_PRIVATE)
}
