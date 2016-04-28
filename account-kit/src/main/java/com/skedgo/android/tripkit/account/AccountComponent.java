package com.skedgo.android.tripkit.account;

import com.skedgo.android.tripkit.Configs;
import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.account.api.AccountApi;
import com.skedgo.android.tripkit.account.api.ApiModule;
import com.skedgo.android.tripkit.account.api.UserTokenStore;
import com.skedgo.android.tripkit.scope.ExtensionScope;

import dagger.Component;

/**
 * To initialize this, refer to {@link DaggerAccountComponent#builder()}.
 */
@ExtensionScope
@Component(
    dependencies = TripKit.class,
    modules = ApiModule.class
)
public interface AccountComponent {
  AccountApi accountApi();

  /**
   * @return A store to retrieve and persist user token obtained via {@link AccountApi}.
   * This might be supplied to {@link Configs#userTokenProvider()} so that
   * TripKit can send appropriate booking requests.
   */
  UserTokenStore userTokenStore();
}